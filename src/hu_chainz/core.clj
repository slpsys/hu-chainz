(ns hu-chainz.core
  (:require [hu-chainz.common :refer :all]))

(defn tokenize-line
  "Tokenizes...the line"
  [line]
  (clojure.string/split line #"[\s\t\n]"))

(defn token-to-chain
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

(defn token-to-map
  [token-chain existing-map]
  (let [updated-map (update-map (first token-chain) existing-map)]
    (if (has-more? token-chain)
      (recur (rest token-chain) updated-map)
      updated-map)))

(defn feed
  "I don't do a whole lot."
  [text]
  (-> text tokenize-line construct-state-map))
