(ns hu-chainz.common)

; Int funcs
(def incr (partial + 1))

(def decr (partial - 1))

; Seq funcs
(def first-string (comp str first))

(defn has-more? [my-seq] (if (seq (rest my-seq)) true false))

(def zip (partial map vector))

; Logic funcs
; Is this defined somewhere common? It has to be, right?
(defmacro unless [pred a b]
	`(if (not ~pred) ~a ~b))
