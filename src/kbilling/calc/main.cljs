(ns kbilling.calc.main
  (:require [kbilling.calc.plans :as p]
            [kbilling.calc.transform :as tf]
            [cljs.nodejs :as node]
            [cognitect.transit :as t]))

(def Decimal (js/require "decimal.js"))
(def express (js/require "express"))
(def body-parser (js/require "body-parser"))
(def serve-static (js/require "serve-static"))

(def app (express))
(def port (or (-> js/process .-env .-PORT) 8000))

(def w (t/writer :json {:handlers {Decimal (t/write-handler (fn [_] "f") #(.toString %))}}))
(def r (t/reader :json {:handlers {"f" #(Decimal. %)}}))

(defn apply-transform [f s] (->> s (t/read r) (f) (t/write w)))

(defn say-hi [req res] (.send res "How are you doing ;)"))

(defn process-transit [load-plan]
  (fn [req res] (.send res (apply-transform #(tf/transform load-plan %) (.-body req)))))

(defn do-post [plans-dir]
  (fn [req res] (.format res (clj->js {"application/transit+json" (process-transit (p/mk-load-plan plans-dir))}))))

(defn -main [plans-dir & args]
  (.use app (.text body-parser (clj->js {:type "application/*"})))
  (.get app "/" say-hi)
  (.post app "/" (do-post plans-dir))
  (.use app (serve-static plans-dir))
  (.listen app port #(.log js/console "Listening on port" port)))


;(node/enable-util-print!)
(set! *main-cli-fn* -main)
