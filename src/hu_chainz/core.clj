(ns hu-chainz.core
  (:require [hu-chainz.common :refer :all]))

(defn tokenize-line
  "Tokenizes...the line"
  [line]
  (clojure.string/split line #"[\s\t\n]"))

(def first-string (comp str first))

(defn token-to-chain2
  ""
  [token-list]
  (let [first-pair [:start (first token-list)]]
    (cond
      (not (seq token-list)) [[:start :end]]
      (not (has-more? token-list)) [first-pair [(first token-list) :end]]
      :else (cons first-pair (zip token-list (conj (vec (rest token-list)) :end))))))

(defn token-to-chain
  "Converts a chain "
  [token-list]
  (loop [tokens token-list
         last-token :start
         cur-token (if (seq tokens) (first-string tokens) :end)
         chain []]
    (let [nu-chain (cons [last-token cur-token] chain)]
      (unless (seq tokens)
        nu-chain
        (recur (rest tokens)
               cur-token
               (if (has-more? tokens) (second tokens) :end)
               nu-chain)))))

(defn construct-state-map
  ""
  [token-list]
  (loop [tokens token-list
         cur-token (if (seq tokens) (first-string tokens) :end)
         last-token :start
         states {last-token {cur-token 1}}]
    (let [last-state (get states last-token)]
      (prn last-state)
      (if (contains? last-state cur-token)
        (assoc last-state cur-token (-> cur-token (get last-state) incr))
        (assoc last-state cur-token 1)))))
  ;  ()
  ;  (prn states)
  ;  (unless (seq tokens)
  ;    (assoc states last-state (assoc (get states last-state) :end 1))
  ;    (let [])
  ;    (if (contains? states (first-string tokens))
  ;      (assoc states last-state (assoc (get states last-state) :end 1))
  ;    nil)))

(defn feed
  "I don't do a whole lot."
  [text]
  (-> text tokenize-line construct-state-map))
