(defproject kbilling-plans-cljs "0.1.0-SNAPSHOT"
  :description "KillingBilling JS plans framework"
  :url "https://www.killingbilling.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2280"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild {:test-commands {"unit-tests" ["node" "target/test.js"]}
              :builds        [{:source-paths ["src"]
                               :compiler     {:target        :nodejs
                                              :output-to     "target/main.js"
                                              :optimizations :advanced
                                              :pretty-print  true}}
                              {:source-paths ["test"]
                               :compiler     {:target        :nodejs
                                              :output-to     "target/test.js"
                                              :optimizations :simple
                                              :pretty-print  true}}]}

  :profiles {:dev {:plugins      [[lein-cljsbuild "1.0.3"]
                                  [lein-npm "0.4.0"]]
                   :dependencies [[org.bodil/cljs-noderepl "0.1.11"]
                                  [com.cemerick/clojurescript.test "0.3.1"]]}}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]} ;Node.js ClojureScript REPL support

  )
