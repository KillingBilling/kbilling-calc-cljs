(ns kbilling.plans
  (:require [kbilling.plans.transform :as tf]))


(def load-plan
  (memoize (fn [path]
             (let [p (js->clj (js/require (str (.cwd js/process) "/" path)) :keywordize-keys true)
                   f (fn f [x]
                       (cond (map? x) (into {} (for [[k v] x]
                                                 [k (case k
                                                      :$begin (set (map keyword v))
                                                      (f v))]))
                             :else x))]
               (f p)))))
