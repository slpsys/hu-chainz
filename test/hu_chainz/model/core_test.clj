(ns hu-chainz.model.core-test
  (:require [midje.sweet :refer :all]
            [hu-chainz.model.core :refer :all]))

(facts "about `tokenize`"
  (fact "it yields an empty seq for nil/emtpy strings"
    (tokenize nil) => '()
    (tokenize "")  => '())
  (fact "it yields seq of first argument without split strings"
    (tokenize "abcdefgh")   => '("abcdefgh"))
  (fact "it splits into seq on expected boundaries"
    (tokenize "a\tb\tc\td") => '("a" "b" "c" "d")
    (tokenize "a\nb\nc\nd") => '("a" "b" "c" "d")
    (tokenize "a b c d")    => '("a" "b" "c" "d")))

(facts "about `token-list-to-chain`"
  (fact "it yields trivial case for nil or empty seq"
    (token-list-to-chain nil) => [[:start :end]]
    (token-list-to-chain '()) => [[:start :end]])
  (fact "it yields start-token-end for single-token lists"
    (token-list-to-chain '("a")) => [[:start "a"] ["a" :end]])
  (fact "it yields more complex start-tokens-end bigrams for multi-element lists"
    (token-list-to-chain '("a" "b" "c")) => [[:start "a"] ["a" "b"] ["b" "c"] ["c" :end]]))

(facts "about `update-map`"
  (fact "it yields a nil-to-nil for weird nil/empty inputs"
    (update-map nil nil) => {nil {nil 1}}
    (update-map [] nil)  => {nil {nil 1}}
    (update-map nil {})  => {nil {nil 1}})
  (fact "it yields a token-pair singleton map for valid token pairs with a nil or empty map "
    (update-map [:start :end] nil) => {:start {:end 1}}
    (update-map [:start :end] {})  => {:start {:end 1}})
  (fact "it appends start-to-end token with a count 1 to an existing map for valid token pairs"
    (update-map ["a" "b"] {:start {:end 1}})   => {"a" {"b" 1} :start {:end 1}})
  (fact "it yields an incremented inner map when a first/second key pair already exist"
    (update-map [:start "a"] {:start {:end 1}}) => {:start {:end 1 "a" 1}}
    (update-map [:start "a"] {:start {:end 1} "a" {"b" 1}}) => {:start {:end 1 "a" 1} "a" {"b" 1}}))

(facts "about `chain-to-map`"
  (fact "it yields a nil-to-nil map for weird nil/empty inputs"
    (chain-to-map nil ) => {nil {nil 1}}
    (chain-to-map [] )  => {nil {nil 1}})
  (fact "it builds a trivial start/end map for single-pair inputs with no map"
    (chain-to-map [[:start :end]]) => {:start {:end 1}})
  (fact "it builds a sparse map of one-valued pairs for a disjoint token chain"
    (chain-to-map [[:start :end] ["a" "b"]]) => {:start {:end 1} "a" {"b" 1}})
  (fact "it builds an updated map of one-valued pairs for a non-disjoint, non-overlapping token chain"
    (chain-to-map [[:start "a"] ["a" :end]]) => {:start {"a" 1} "a" {:end 1}})
  (fact "it builds an updated map of one-valued pairs for a non-disjoint, overlapping, redundant token chain"
    (chain-to-map [[:start "a"] ["a" :end] ["a" "b"] [:start "a"]]) => {:start {"a" 2} "a" {:end 1 "b" 1}}))
