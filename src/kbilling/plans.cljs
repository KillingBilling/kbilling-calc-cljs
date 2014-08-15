(ns kbilling.plans
  (:require [kbilling.plans.transform :as tf]))

(def reflect-fn (js/require "function-to-string"))

(defn load-cost-fn [f path]
  (let [params (vec (map keyword (.-params (reflect-fn f))))] ;TODO prefix local params with cycle key
    (fn [vars] (apply f (map #(vars %) params)))))          ;TODO test

(defn load-v [x path]
  (cond
    (map? x)
    (into {} (for [[k v] x]
               [k (case k
                    :$begin (set (map keyword v))
                    :$cost (load-cost-fn v path)
                    (load-v v (conj path k)))]))

    :else x))

(defn root-require [path]
  (js/require (str (.cwd js/process) "/" path)))

(def load-plan
  (memoize #(load-v (js->clj (root-require %) :keywordize-keys true) [])))
