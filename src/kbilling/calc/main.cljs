(ns kbilling.calc.main
  (:require [kbilling.calc.transform :as tf]
            [kbilling.calc.plans :as p]
            [cljs.nodejs :as node]
            [cognitect.transit :as t]))

(def split (js/require "split"))

(def w (t/writer :json))
(def r (t/reader :json))

(defn apply-transform [f s]
  (try
    (case s "" "" (->> s (t/read r) (f) (t/write w)))
    (catch js/Error e
      (.error js/console (.-stack e))
      "")))

(defn pipe-through [f in out]
  (-> in (.pipe (split #(str (f %) "\n"))) (.pipe out)))

(defn -main [& args]
  (pipe-through #(apply-transform tf/transform %) (.-stdin js/process) (.-stdout js/process)))

;(node/enable-util-print!)
(set! *main-cli-fn* -main)
