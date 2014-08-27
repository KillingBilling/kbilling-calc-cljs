(ns kbilling.calc.transform-test
  (:require-macros [cemerick.cljs.test :refer [is deftest with-test run-tests testing test-var]])
  (:require [kbilling.calc.plans :as p]
            [kbilling.calc.transform :as tf]
            [cemerick.cljs.test :as t]))

(def BigNumber (js/require "bignumber.js"))

(defn =v [m1 m2]
  (let [str-v (fn [[k v]] [k (str v)])]
    (= (into {} (map str-v m1)) (into {} (map str-v m2)))))


(deftest k_-test
  (is (= (tf/k_ :a :bb :$cost)
         :a_bb_$cost)))

(deftest big-number-construction
  (is (=v {:coverage (BigNumber. 200)
           :rub      (.times (BigNumber. 200) (BigNumber. 0.3))}
          {:coverage 200
           :rub      60})))

(def basic-plan (p/load-plan "test/kbilling/plans/examples/basic"))

(deftest aggregate-test
  (is (=v (tf/aggregate basic-plan #{:monthly} {} {:coverage 200})
          {:monthly_coverage_sum 200})))

(deftest calculate-costs-test
  (is (=v (tf/calculate-costs basic-plan #{:monthly} {:coverage 200, :monthly_coverage_sum 200})
          {:monthly_rub_$cost 60, :$subscription_rub_$cost 2800})))

(deftest apply-costs-test
  (is (=v (tf/apply-costs {:rub 350, :monthly_rub_$cost 10, :$subscription_rub_$cost 2700}
                          {:monthly_rub_$cost 60, :$subscription_rub_$cost 2800})
          {:rub 200})))

(deftest calculate-test
  (is (=v (tf/calculate-no-values basic-plan #{:monthly} {} {:coverage 200, :monthly_coverage_sum 200})
          {:$subscription_rub_$cost 2800, :monthly_rub_$cost 60, :rub -2860}))

  (is (=v (tf/calculate basic-plan #{:monthly}
                        {:$subscription_rub_$cost 2800}
                        {:coverage 200, :monthly_coverage_sum 200})
          {:$subscription_rub_$cost 2800, :monthly_rub_$cost 60, :rub -60, :rubOrCost 60})))

(deftest merge-with+bign-test
  (is (=v (merge-with tf/+bign {:rub 1000, :$subscription_rub_$cost 2800} {:rub 3000} {:coverage 200})
          {:rub 4000, :$subscription_rub_$cost 2800, :coverage 200})))

(deftest apply-add-buy-test
  (is (=v (tf/apply-add-buy basic-plan #{:monthly}
                            {:rub 1000, :$subscription_rub_$cost 2800}
                            {:rub 3000}
                            {:coverage 200})
          {:rub                     3940
           :coverage                200
           :monthly_coverage_sum    200
           :$subscription_rub_$cost 2800
           :monthly_rub_$cost       60
           :rubOrCost               3940})))


(deftest transitive-billing-cycles-test
  (is (= (tf/transitive-billing-cycles basic-plan :$subscription)
         #{:$subscription :monthly}))
  (is (= (tf/transitive-billing-cycles basic-plan :monthly)
         #{:monthly})))

(deftest acc-keys-test
  (is (= #{:rub :coverage}
         (tf/acc-keys basic-plan))))

(deftest init-vars-test
  (is (=v (tf/init-vars basic-plan #{:$subscription :monthly} {})
          {:rub                     0
           :coverage                0
           :monthly_coverage_sum    0
           :monthly_rub_$cost       0
           :$subscription_rub_$cost 0}))

  (is (=v (tf/init-vars basic-plan #{:monthly} {:rub 1000, :coverage 70000, :$subscription_rub_$cost 2800})
          {:rub                     1000
           :coverage                70000
           :monthly_coverage_sum    0
           :monthly_rub_$cost       0
           :$subscription_rub_$cost 2800})))

(deftest cycle-begin-test
  (is (=v (tf/cycle-begin basic-plan :monthly {:rub 1000, :coverage 70000, :$subscription_rub_$cost 2800})
          {:rub                     1000
           :coverage                70000
           :monthly_coverage_sum    0
           :monthly_rub_$cost       0
           :$subscription_rub_$cost 2800
           :rubOrCost               1000})))

(deftest subscribe-test
  (is (=v (tf/subscribe basic-plan {})
          {:rub                     -2800
           :coverage                0
           :monthly_coverage_sum    0
           :$subscription_rub_$cost 2800
           :monthly_rub_$cost       0
           :rubOrCost               0})))

(deftest notifications-test
  (is (= (tf/notifications basic-plan {:rub 100})
         {:rubBelow0 false}))
  (is (= (tf/notifications basic-plan {:rub 0})
         {:rubBelow0 false}))
  (is (= (tf/notifications basic-plan {:rub -10})
         {:rubBelow0 true})))
