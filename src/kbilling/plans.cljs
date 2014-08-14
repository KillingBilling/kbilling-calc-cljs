(ns kbilling.plans
  (:require [kbilling.plans.transform :as tf]))


(def load-plan (memoize #(js->clj (js/require (str (.cwd js/process) "/" %)) :keywordize-keys true)))
