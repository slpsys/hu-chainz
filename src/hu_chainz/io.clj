(ns hu-chainz.io
  (:require [clojure.java.io :as io]
            [cognitect.transit :as t]
            [hu-chainz.model.core :refer [feed generate]])
  (:import
     (java.io InputStreamReader)))

(defn stringify [tokens] (clojure.string/join " " tokens))

(defn create-model
  "Builds a model from a local filename."
  [fname]
  (-> fname slurp feed))

(defn write-model
  "Writes a model to disk."
  [model fname]
  (with-open [fh (clojure.java.io/output-stream fname)]
             (t/write (t/writer fh :msgpack) model)))

(defn read-model
  [fname]
  (let
    [in-stream (io/input-stream fname)
     rdr       (t/reader in-stream :msgpack)]
  (t/read rdr)))
