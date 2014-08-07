(ns kbilling.plans
  (:require [kbilling.plans.transform :as tf]
            [cljs.nodejs :as node]))


(def load-plan (memoize (fn [path] (js->clj (js/require (str (.cwd js/process) "/" path))))))


(defn -main [& args]
  (println "Hello"))

(node/enable-util-print!)
(set! *main-cli-fn* -main)
