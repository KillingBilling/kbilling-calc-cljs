(ns kbilling.calc.main-test
  (:require-macros [cemerick.cljs.test :refer [is deftest with-test run-tests testing test-var]])
  (:require [kbilling.calc.main :as main]
            [cognitect.transit :as t]
            [cemerick.cljs.test]))

(defn some-transform [{:keys [a b]}] {:z (+ a 3) :y (+ a b) :x (* b 5)})

(deftest apply-transform-test
  (let [w (t/writer :json)
        o {:a 1 :b 2}]
    (println (t/write w o))
    (is (= (main/apply-transform some-transform (t/write w o))
           (t/write w (some-transform o))))))
