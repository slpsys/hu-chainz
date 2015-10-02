(ns hu-chainz.io
  (:require [clojure.java.io :as io]
            [cognitect.transit :as t]
            [hu-chainz.model.core :refer [feed generate]])
  (:import
     (java.io InputStreamReader)))

(defn stringify [tokens] (clojure.string/join " " tokens))

(def talk (comp stringify generate))

(defn tell-me-about
  "Builds strings randomly until it finds one that contains a particular string. Useful for proper nouns you know are in the corpus."
  ([something] (tell-me-about something 1000000))
  ([something up-to]
   (take 1
     (filter
       #(.contains % something)
       (repeatedly up-to #(talk my-model))))))

(defn create-model
  "Builds a model from a local filename."
  [fname]
  (-> fname slurp feed))

(defn read-model
  [fname]
  (let
    [in-stream (io/input-stream fname)
     rdr       (t/reader in-stream :msgpack)]
  (t/read rdr)))

(defn write-model
  "Writes a model to disk."
  [model fname]
  (with-open [fh (clojure.java.io/output-stream fname)]
             (t/write (t/writer fh :msgpack) model)))
