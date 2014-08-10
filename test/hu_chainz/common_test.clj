(ns hu-chainz.common-test
  (:require [midje.sweet :refer :all]
            [hu-chainz.common :refer :all]))

(facts "about `incr`"
  (fact "it increments integers"
    (incr 0) => 1
    (incr 4294967295) => 4294967296))

(facts "about `decr`"
  (fact "it decrements integers"
    (decr 1) => 0
    (decr 0) => 1)) ; lolwut

(facts "about `first-string`"
  (fact "it returns an empty string for null or empty seqs"
    (first-string nil) => ""
    (first-string '()) => ""
    (first-string []) => ""
    (first-string [nil]) => "")
  (fact "it returns a stringified version of the first object"
    (first-string [1]) => "1"
    (first-string [:a]) => ":a"
    (first-string [1 2 3 4]) => "1"
    (first-string [(reify Object (toString [_] "lol")) :herp :derp ]) => "lol"))

(facts "about `has-more?`"
  (fact "it yields false for a nil or empty seq"
    (has-more? nil) => false
    (has-more? '()) => false
    (has-more? []) => false)
  (fact "it yields false for a seq of len 1"
    (has-more? '(1)) => false
    (has-more? [1]) => false)
  (fact "it yields true for a non-empty seq of size 2 or greater"
    (has-more? '(1 2)) => true
    (has-more? (take (+ 2 (int (rand 100000))) (repeat 0))) => true))

(facts "about `zip`"
  (fact "it yields an empty seq for arity one"
    (zip nil) => '()
    (zip []) => '()
    (zip '()) => '())
  (fact "it yields an empty seq nils or empty seqs"
    (zip nil nil) => '()
    (zip nil '()) => '()
    (zip '() '()) => '())
  (fact "it yields a normally-zipped list for two non-empty, same-len seqs"
    (zip [1] [2]) => '([1 2])
    (zip [1 3 5 7] [2 4 6 8]) => '([1 2] [3 4] [5 6] [7 8]))
  (fact "it yields a normally-zipped list with a len of the shorterfor two non-empty diff-len seqs"
    (zip [1] [2 3]) => '([1 2])
    (zip [1 3 5 7 9] [2 4 6 8]) => '([1 2] [3 4] [5 6] [7 8])))

(facts "about `unless`"
  (fact "I can't believe there isn't something like this in stdlib"
    true => true)
  (fact "it works as advertised"
    (unless true 1 0) => 0
    (unless false 1 0) => 1))
