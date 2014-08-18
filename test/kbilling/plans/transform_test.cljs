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
    (= (into {} (map str-v m1)) (into {} (map str-v m2)))))


(deftest k_-test
  (is (= :a_bb_$cost
         (tf/k_ :a :bb :$cost))))

(deftest big-number-construction
  (is (=v {:coverage 200
           :rub      60}
          {:coverage (BigNumber. 200)
           :rub      (.times (BigNumber. 200) (BigNumber. 0.3))})))

(def basic-plan (p/load-plan "test/kbilling/plans/examples/basic"))

(deftest aggregate-test
  (is (=v {:monthly_coverage_sum 200}
          (tf/aggregate basic-plan #{:monthly} {} {:coverage 200}))))

(deftest calculate-costs-test
  (is (=v {:monthly_rub_$cost 60, :$subscription_rub_$cost 2800}
          (tf/calculate-costs basic-plan #{:monthly} {:coverage 200, :monthly_coverage_sum 200}))))

(deftest apply-costs-test
  (is (=v {:rub 200}
          (tf/apply-costs {:rub 350, :monthly_rub_$cost 10, :$subscription_rub_$cost 2700}
                          {:monthly_rub_$cost 60, :$subscription_rub_$cost 2800}))))

(deftest calculate-test

  (is (=v {:$subscription_rub_$cost 2800, :monthly_rub_$cost 60, :rub -2860}
          (tf/calculate-no-values basic-plan #{:monthly} {} {:coverage 200, :monthly_coverage_sum 200})))

  (is (=v {:$subscription_rub_$cost 2800, :monthly_rub_$cost 60, :rub -60, :rubOrCost 60}
          (tf/calculate basic-plan #{:monthly}
                        {:$subscription_rub_$cost 2800}
                        {:coverage 200, :monthly_coverage_sum 200}))))

(deftest merge-with+bign-test
  (is (=v {:rub 4000, :$subscription_rub_$cost 2800, :coverage 200}
          (merge-with tf/+bign {:rub 1000, :$subscription_rub_$cost 2800} {:rub 3000} {:coverage 200}))))

(deftest apply-add-buy-test
  (is (=v {:rub                     3940
           :coverage                200
           :monthly_coverage_sum    200
           :$subscription_rub_$cost 2800
           :monthly_rub_$cost       60
           :rubOrCost               3940}
          (tf/apply-add-buy basic-plan #{:monthly}
                            {:rub 1000, :$subscription_rub_$cost 2800}
                            {:rub 3000}
                            {:coverage 200}))))