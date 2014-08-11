(ns hu-chainz.model.core-test
  (:require [midje.sweet :refer :all]
            [hu-chainz.model.core :refer :all]))

(facts "about `tokenize`"
	(fact "it yields an empty seq for nil/emtpy strings"
		(tokenize nil) => '()
		(tokenize "") => '())
	(fact "it yields seq of first argument without split strings"
		(tokenize "abcdefgh") => '("abcdefgh"))
	(fact "it splits into seq on expected boundaries"
		(tokenize "a\tb\tc\td") => '("a" "b" "c" "d")
		(tokenize "a\nb\nc\nd") => '("a" "b" "c" "d")
		(tokenize "a b c d") => '("a" "b" "c" "d")))

(facts "about `token-list-to-chain`"
	(fact "it yields trivial case for nil or empty seq"
		(token-list-to-chain nil) => [[:start :end]]
		(token-list-to-chain '()) => [[:start :end]])
	(fact "it yields start-token-end for single-token lists"
		(token-list-to-chain '("a")) => [[:start "a"] ["a" :end]])
	(fact "it yields more complex start-tokens-end bigrams for multi-element lists"
		(token-list-to-chain '("a" "b" "c")) => [[:start "a"] ["a" "b"] ["b" "c"] ["c" :end]]))
