(ns kbilling.calc.transform-test
  (:require-macros [cemerick.cljs.test :refer [is deftest with-test run-tests testing test-var]])
  (:require [kbilling.calc.plans :as p]
            [kbilling.calc.transform :as tf]
            [cemerick.cljs.test]))

(def Decimal (js/require "decimal.js"))


(deftest k_-test
  (is (= (tf/k_ :a :bb :$cost)
         :a_bb_$cost)))

(deftest big-number-construction
  (is (= {:coverage (Decimal. 200)
          :rub      (.times (Decimal. 200) (Decimal. 0.3))}
         {:coverage 200
          :rub      60})))

(def load-plan (p/mk-load-plan "."))

(def basic-plan-path "test/kbilling/plans/examples/basic")
(def basic-plan (load-plan basic-plan-path))

(deftest aggregate-test
  (is (= (tf/aggregate basic-plan #{:monthly} {} {:coverage 200})
         {:monthly_coverage_sum 200})))

(deftest calculate-costs-test
  (is (= (tf/calculate-costs basic-plan #{:monthly} {:coverage 200, :monthly_coverage_sum 200})
         {:monthly_rub_$cost 60})))

(deftest apply-costs-test
  (is (= (tf/apply-costs {:rub 350, :monthly_rub_$cost 10, :$subscription_rub_$cost 2700}
                         {:monthly_rub_$cost 60, :$subscription_rub_$cost 2800})
         {:rub 200})))

(deftest calculate-test
  (is (= (tf/calculate-no-values basic-plan #{:monthly} {} {:coverage 200, :monthly_coverage_sum 200})
         {:monthly_rub_$cost 60, :rub -60}))

  (is (= (tf/calculate basic-plan #{:monthly} {} {:coverage 200, :monthly_coverage_sum 200})
         {:monthly_rub_$cost 60, :rub -60, :rubOrCost 60})))

(deftest merge-with+bign-test
  (is (= (merge-with tf/+bign {:rub 1000, :$subscription_rub_$cost 2800} {:rub 3000} {:coverage 200})
         {:rub 4000, :$subscription_rub_$cost 2800, :coverage 200})))

(deftest apply-add-buy-test
  (is (= (tf/add-buy basic-plan #{:monthly}
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
  (is (= (tf/acc-keys basic-plan)
         #{:rub :coverage})))

(deftest init-vars-test
  (is (= (tf/init-vars basic-plan #{:$subscription :monthly} {})
         {:rub                     0
          :coverage                0
          :monthly_coverage_sum    0
          :monthly_rub_$cost       0
          :$subscription_rub_$cost 0}))

  (is (= (tf/init-vars basic-plan #{:monthly} {:rub 1000, :coverage 70000})
         {:rub                  1000
          :coverage             70000
          :monthly_coverage_sum 0
          :monthly_rub_$cost    0}))

  (is (= (tf/init-vars basic-plan #{:daily} {:rub 1000, :coverage 70000})
         {:rub             1000
          :coverage        70000
          :daily_rub_$cost 0})))

(deftest cycle-begin-test
  (is (= (tf/cycle-begin basic-plan :monthly {:rub 1000, :coverage 70000})
         {:rub                  1000
          :coverage             70000
          :monthly_coverage_sum 0
          :monthly_rub_$cost    0
          :rubOrCost            1000}))
  (is (= (tf/cycle-begin basic-plan :daily {:rub      1000
                                            :coverage 0})
         {:rub             1000
          :daily_rub_$cost 0
          :coverage        0})))

(deftest subscribe-test
  (is (= (tf/subscribe basic-plan {})
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

;TODO (imikushin) write proper test
(deftest transform-test
  (println (tf/transform (p/mk-load-plan ".")
                         {:a [:cycle-begin basic-plan-path :monthly {:rub 1000, :coverage 70000, :$subscription_rub_$cost 2800}]
                          :b []})))
