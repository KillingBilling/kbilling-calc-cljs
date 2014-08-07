(ns kbilling.plans
  (:require [cljs.nodejs :as node]))


(defn -main [& args]
  (println "Hello"))


(node/enable-util-print!)
(set! *main-cli-fn* -main)
