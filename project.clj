(defproject hu-chainz "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.cognitect/transit-clj "0.8.255"]
                 [org.clojure/clojure "1.6.0"]
                 [twitter-api "0.7.6"]]
  :profiles {:dev {:dependencies [[midje "1.6.3"]]}})
