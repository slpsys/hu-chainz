(ns hu-chainz.model.core
  (:require [hu-chainz.common :refer :all]))

(defn tokenize
  "Tokenizes...the text"
  [line]
  (remove #(= "" %) (clojure.string/split line #"[\s\t\n]")))

(defn token-list-to-chain
  "Converts a sequence of tokens to a sequence of ordered pairs,
    e.g. ('a' 'b') => ([:start 'a'] ['a' 'b'] ['b' :end])"
  [token-list]
  (let [first-pair [:start (first token-list)]]
    (cond
      (not (seq token-list)) [[:start :end]]
      (not (has-more? token-list)) [first-pair [(first token-list) :end]]
      :else (cons first-pair (zip token-list (conj (vec (rest token-list)) :end))))))

(defn update-map
  [token-pair directed-map]
  (let [first-token (first token-pair)
        second-token (second token-pair)]
    (unless (contains? directed-map first-token)
      (assoc directed-map first-token {second-token 1 })
      (let [inner-map (get directed-map first-token)]
        (assoc directed-map
               first-token
               (if (get inner-map second-token)
                 (assoc inner-map
                             second-token
                             (incr (get inner-map second-token)))
                 (assoc inner-map second-token 1)))))))

(defn chain-to-map
  ([token-chain] (chain-to-map token-chain {}))
  ([token-chain existing-map]
   (let [updated-map (update-map (first token-chain) existing-map)]
     (if (has-more? token-chain)
       (recur (rest token-chain) updated-map)
       updated-map))))

(defn textulate
  "I don't do a whole lot."
  [text & {:keys [tokenizer-fn] :or {tokenizer-fn tokenize}}]
    (-> text tokenizer-fn token-list-to-chain chain-to-map))

(defn merge-nested-maps
  [larger-map smaller-map]
  (let [unconflicted-keys (clojure.set/difference (set (keys smaller-map)) (set (keys larger-map)))
        conflicted-keys (clojure.set/intersection (set (keys smaller-map)) (set (keys larger-map)))
        even-larger-map (merge larger-map (select-keys smaller-map unconflicted-keys))]
    (merge even-larger-map
           (reduce #(assoc %1 %2 (merge-with + (get larger-map %2) (get smaller-map %2))) {} conflicted-keys))))

(defn feed
  [corpus & {:keys [tokenizer-fn phrase-boundary-fn]
              :or {tokenizer-fn tokenize phrase-boundary-fn clojure.string/split-lines}}]
    (let [phrases (phrase-boundary-fn corpus)]
      (reduce merge-nested-maps (map textulate phrases))))
