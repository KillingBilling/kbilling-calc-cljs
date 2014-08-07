(ns kbilling.plans.main
  (:require [kbilling.plans.transform :as t]
            [cljs.nodejs :as node]))


(defn -main [& args]
  (println "Hello"))

(node/enable-util-print!)
(set! *main-cli-fn* -main)
