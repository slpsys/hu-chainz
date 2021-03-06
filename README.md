# hu-chainz

Yet Another Markov Chain library/bot in Clojure

## To-do

1. Streaming `feed`
2. Test out [r/w] serialization time, size of Transit formats on models
3. Add helper script/code to pull down/clean text from Twitter accounts

## Usage

`feed` takes a single document, containing a set of phrases, each composed of a set of tokens. Optionally, you can provide both a document-splitting function and a tokenizing function; the defaults are `clojure.string/split-lines` for the document splitter, and `hu-chainz.model/tokenize` for the tokenizer.

With an example, newline-delimited document:

    user=> (require ['hu-chainz.model.core :refer ['feed]])
    nil

    user=> (def corpus "i am a cat
      #_=> i am a dog
      #_=> i am the walrus")
    #'user/corpus

    user=> (feed corpus)
    {"walrus" {:end 1}, "the" {"walrus" 1}, "dog" {:end 1}, "cat" {:end 1}, "a" {"dog" 1, "cat" 1}, "am" {"the" 1, "a" 2}, "i" {"am" 3}, :start {"i" 3}}

While it's not integrated yet, there's nothing stopping you from `map`ping the result of `feed` over a set of documents, and merging the results into a combined model using `merge-nested-maps`:

    user=> (def opus "dogs rule
      #_=> cats drool")
    #'user/opus

    user=> (let [[cmodel omodel] (map feed [corpus opus])]
      #_=>   (merge-nested-maps cmodel omodel))
    {"dog" {:end 1}, "rule" {:end 1}, "drool" {:end 1}, "cats" {"drool" 1}, :start {"dogs" 1, "cats" 1, "i" 3}, "am" {"the" 1, "a" 2}, "a" {"dog" 1, "cat" 1}, "i" {"am" 3}, "walrus" {:end 1}, "cat" {:end 1}, "dogs" {"rule" 1}, "the" {"walrus" 1}}

You can also easily serialize your models via [transit](http://cognitect.github.io/transit-clj/)

    user=> (require '[clojure.java.io :as io]
                    '[cognitect.transit :as t])
    nil

    user=> (with-open [fh (clojure.java.io/output-stream "/tmp/model")]
      #_=>   (t/write (t/writer fh :msgpack) (feed corpus)))
    nil

Note that cats drooling is tautological in this model.

The constructed model can then be fed directly into `generate`, which returns a sequence of strings:

    user=> (stringify (generate model))
    "i am a dog"
    user=> (stringify (generate model))
    "i am a cat"
    user=> (stringify (generate model))
    "i am a cat"
    user=> (stringify (generate model))
    "i am the walrus"

## Suggested Corpii

### Twitter Dump

You can request a full export of all of your tweets [here](https://twitter.com/settings/account), and after a few hours, receive a .zip archive that contains a static HTML app displaying your tweets. The tweet data itself is encapsulated in a series of Javascript/JSON files, under `data/js/tweets/`. I wrote a [quick Ruby script](https://gist.github.com/slpsys/fdea5354fec81b33fcd7) (in the interst of time) to dump the tweet data into a line-delimited single file. I just ran it quickly from `irb`, and it should give you a fairly clean corpus from which to build a model that sounds like a messed-up version of yourself.

Build, use, and store your model:

    user=> (def my-model (create-model "/Users/marc/tmp/tweetstorm.txt"))
    #'user/my-model
    user=> (-> my-model generate stringify) ;; Real output from my tweets
    "This is feature-complete. Time elapsed before it's Friday, disheartenment a hot dog bed, receive dog."
    ...
    user=> (write-model my-model "/tmp/tweetstorm.model")
    nil

## License

Copyright © 2014 Marc Bollinger

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
