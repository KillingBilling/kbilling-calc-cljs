(defproject
  kbilling-calc-cljs "0.1.0-SNAPSHOT"
  :description "KillingBilling JS plan calculation framework"
  :url "https://www.killingbilling.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :repositories [["sonatype" {:url       "https://oss.sonatype.org/content/repositories/releases"
                              :snapshots false}]]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"]
                 [com.cognitect/transit-cljs "0.8.199"]
                 [org.clojure/core.match "0.2.2"]]

  :node-dependencies [[body-parser "^1.9.3"]
                      [decimal.js "^4.0.0"]
                      [express "^4.10.4"]
                      [function-to-string "^0.2.0"]
                      [serve-static "^1.7.1"]
                      [source-map-support "^0.2.7"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild {:test-commands {"unit-tests" ["node" "./run-tests.js"]}
              :builds        {:main {:source-paths ["src"]
                                     :compiler     {:target        :nodejs
                                                    :output-to     "target/main.js"
                                                    :output-dir    "target/main"
                                                    :optimizations :none
                                                    :source-map    true
                                                    :pretty-print  true}}
                              :test {:source-paths   ["src" "test"]
                                     :notify-command ["node" "./run-tests.js"]
                                     :compiler       {:target        :nodejs
                                                      :output-to     "target/test.js"
                                                      :output-dir    "target/test"
                                                      :optimizations :none
                                                      :source-map    true
                                                      :pretty-print  true}}}}

  :profiles {:dev {:plugins      [[lein-cljsbuild "1.0.4"]
                                  [lein-npm "0.5.0"]]
                   :dependencies [[org.bodil/cljs-noderepl "0.1.11"]
                                  [com.cemerick/clojurescript.test "0.3.3"]]}}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]} ;Node.js ClojureScript REPL support

  )
