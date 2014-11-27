(ns kbilling.calc.main
  (:require [cljs.reader :as reader]
            [kbilling.calc.transform :as tf]
            [kbilling.calc.plans :as p]
            [cljs.nodejs :as node]
            [cognitect.transit :as t]))

(def express (js/require "express"))
(def bodyParser (js/require "body-parser"))

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
  (let [app (express)
        port 8000]  ; TODO use an arg or PORT env var
    (.use app (.text bodyParser (clj->js {:type "application/*"})))
    (.get app "/" (fn [req res] (.send res "Hello World")))
    (.post
      app "/"
      (fn [req res]
        (.format res (clj->js {"application/transit+json" #(.send res (apply-transform tf/transform (.-body req)))}))))

    (.listen app port #(.log js/console "Listening on port" port))))


;; deprecated, previously -main
(defn console-main [& args]
  (pipe-through #(apply-transform tf/transform %) (.-stdin js/process) (.-stdout js/process)))

;(node/enable-util-print!)
(set! *main-cli-fn* -main)
