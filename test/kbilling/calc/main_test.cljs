(ns kbilling.calc.main-test
  (:require-macros [cemerick.cljs.test :refer [is deftest with-test run-tests testing test-var]])
  (:require [kbilling.calc.main :as main :refer [r w]]
            [cognitect.transit :as t]
            [cemerick.cljs.test]))

(def Decimal (js/require "decimal.js"))

(defn some-transform [{:keys [a b]}] {:z (+ a 3) :y (+ a b) :x (* b 5)})
(defn dup [[& xs]] (let [[x] xs] (repeat 2 x)))

(deftest transit-decimal-test
  (let [s "123456.987654"
        d (Decimal. s)]
    (is (= (->> d .toString)
           s))
    (is (= (->> d (t/write w) (t/read r) .toString)
           s))))

(deftest apply-transform-test
  (let [o {:a 1 :b 2}
        a ["42"]]
    (is (= (main/apply-transform some-transform (t/write w o))
           (t/write w (some-transform o))))
    (is (= (main/apply-transform dup (t/write w a))
           (t/write w (dup a))))))
