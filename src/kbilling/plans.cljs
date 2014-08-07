(ns kbilling.plans
  (:require [kbilling.plans.transform :as tf]))


(def load-plan (memoize (fn [path] (js->clj (js/require (str (.cwd js/process) "/" path))))))
