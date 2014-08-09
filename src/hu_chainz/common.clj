(ns hu-chainz.common)

(def incr (partial + 1))

(def decr (partial - 1))

(def zip (partial map vector))

(defn has-more? [my-seq] (if (seq (rest my-seq)) true false))

; Is this defined somewhere common? It has to be, right?
(defmacro unless [pred a b]
	`(if (not ~pred) ~a ~b))

