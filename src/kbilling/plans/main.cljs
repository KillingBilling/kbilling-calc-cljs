(ns kbilling.plans.main
  (:require [kbilling.plans.transform :as t]
            [cljs.nodejs :as node]
            [clojure.string :as string]))

(def split (js/require "split"))

(defn transform [s] (string/upper-case s))

(defn -main [& args]
  (-> js/process
      .-stdin
      (.pipe (split (fn [s] (str (transform s) "\n"))))
      (.pipe (.-stdout js/process))))

;(node/enable-util-print!)
(set! *main-cli-fn* -main)
