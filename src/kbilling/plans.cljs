(ns kbilling.plans
  (:require [kbilling.plans.transform :as tf]))

(def reflect-fn (js/require "function-to-string"))

(defn load-cost-fn [f]
  (let [params (vec (.-params (reflect-fn f)))]                   ;TODO prefix local params with cycle key
    (fn [vars] (apply f (map #(vars %) params)))))                ;TODO test

(def load-plan
  (memoize
    (fn [path]
      (let [p (js->clj (js/require (str (.cwd js/process) "/" path)) :keywordize-keys true)
            f (fn f [x & path]
                (cond
                  (map? x)
                  (into {}
                        (for [[k v] x]
                          [k (case k
                               :$begin (set (map keyword v))
                               :$cost (load-cost-fn v)
                               (apply f v (conj (into [] (case path nil [] path)) k)))]))

                  :else x))]
        (f p)))))
