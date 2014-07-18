(defproject kbilling-plans-js "0.1.0-SNAPSHOT"
  :description "KillingBilling JS plans framework"
  :url "https://www.killingbilling.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/cljs"]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-npm "0.4.0"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                        :compiler {:target :nodejs
                                   :output-to "target/main.js"
                                   :optimizations :simple
                                   :pretty-print true}}]}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2202"]])
