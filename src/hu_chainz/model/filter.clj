(ns hu-chainz.model.filter
  (:require [hu-chainz.common :refer :all]))

(defn- apply-all
	[token fns]
	(reduce #(and %1 (apply %2 [token])) true (vec fns)))

(defn apply-filters
	"Takes a set of tokens and an optional, variadic set of true-to-include filter functions.
	 Each function should take one token as an argument and yield true/false if it should be included."
	[tokens & filter-fns]
	(unless (seq filter-fns)
		tokens
		(filter #(apply-all % filter-fns) tokens)))
