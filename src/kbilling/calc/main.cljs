(ns kbilling.calc.main
  (:require [kbilling.calc.transform :as t]
            [kbilling.calc.plans :as p]
            [cljs.nodejs :as node]
            [clojure.string :as string]))

(def split (js/require "split"))

(defn transform [s] (string/upper-case s))

(defn pipe-through [f in out]
  (-> in (.pipe (split (fn [s] (str (f s) "\n")))) (.pipe out)))

(defn -main [& args]
  (pipe-through transform (.-stdin js/process) (.-stdout js/process)))

;(node/enable-util-print!)
(set! *main-cli-fn* -main)
