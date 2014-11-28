(ns kbilling.calc.main
  (:require [kbilling.calc.transform :as tf]
            [cljs.nodejs :as node]
            [cognitect.transit :as t]))

(def express (js/require "express"))
(def bodyParser (js/require "body-parser"))

(def w (t/writer :json))
(def r (t/reader :json))

(defn apply-transform [f s]
  (try
    (case s "" "" (->> s (t/read r) (f) (t/write w)))
    (catch js/Error e
      (.error js/console (.-stack e))
      "")))

(defn -main [& args]
  (let [app (express)
        port (or (-> js/process .-env .-PORT) 8000)]
    (.use app (.text bodyParser (clj->js {:type "application/*"})))
    (.get app "/" (fn [req res] (.send res "Hello World")))
    (.post
      app "/"
      (fn [req res]
        (.format res (clj->js {"application/transit+json" #(.send res (apply-transform tf/transform (.-body req)))}))))
    (.listen app port #(.log js/console "Listening on port" port))))


;(node/enable-util-print!)
(set! *main-cli-fn* -main)
