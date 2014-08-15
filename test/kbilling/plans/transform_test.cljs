(ns kbilling.plans.transform-test
  (:require-macros [cemerick.cljs.test
                    :refer [is deftest with-test run-tests testing test-var]])
  (:require [kbilling.plans :as p]
            [kbilling.plans.transform :as tf]
            [cemerick.cljs.test :as t]
            [cljs.nodejs :as node]))

(def BigNumber (js/require "bignumber.js"))

(defn =v [m1 m2]
  (let [str-v (fn [[k v]] [k (str v)])]
    (= (map str-v m1) (map str-v m2))))

(deftest $-keyword
  (is (= :a$bb$$cost
         (tf/$ :a :bb :$cost))))

(deftest big-number-construction
  (is (=v {:coverage 200
           :rub      60}
          {:coverage (BigNumber. 200)
           :rub      (.times (BigNumber. 200) (BigNumber. 0.3))})))

(def basic-plan (p/load-plan "test/kbilling/plans/examples/basic"))

(deftest aggregate
  (is (=v {:monthly$coverage$sum 200}
          (tf/aggregate basic-plan #{:monthly} {} {:coverage 200}))))

#_(deftest calculate
  (is (=v {:monthly$rub$$cost 60, :rub -60, :rubOrCost 60}
          (tf/calculate basic-plan #{:monthly} {} {:coverage 200, :monthly$coverage$sum 200}))))
