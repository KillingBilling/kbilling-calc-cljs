(ns kbilling.plans-test
  (:require-macros [cemerick.cljs.test
                    :refer [is deftest with-test run-tests testing test-var]])
  (:require [kbilling.plans :as p]
            [cemerick.cljs.test :as t]))

(def reflect-fn (js/require "function-to-string"))


(def basic-plan (p/load-plan "test/kbilling/plans/examples/basic"))

(deftest global-var-name-test
  (is (= "monthly_coverage_sum"
         (p/global-var-name [:$cycles :monthly :rub] "coverage_sum")))
  (is (= "monthly_coverage_sum"
         (p/global-var-name [:$cycles :monthly :rub] "monthly_coverage_sum")))
  (is (= "daily_coverage_sum"
         (p/global-var-name [:$cycles :monthly :rub] "daily_coverage_sum"))))

(deftest load-fn-test
  (let [f (js/eval "(function(coverage_sum) { return coverage_sum * 0.3 })")
        cost-fn (p/load-fn f [:$cycles :monthly :rub])]
    (is (= 60 (cost-fn {:monthly_coverage_sum 200})))))

(deftest cost-function
  (let [cost-fn (get-in basic-plan [:$cycles :monthly :rub :$cost])]
    (is (= "60" (str (cost-fn {:monthly_coverage_sum 200}))))))

(deftest value-function
  (let [value-fn (get-in basic-plan [:$values :rubOrCost])]
    (is (= "3000" (str (value-fn {:monthly_rub_$cost 3000, :rub 2000}))))))

(deftest load-plan
  (is (= [:$cycles :$values :$notifications]
         (keys basic-plan)))
  (is (= #{:monthly}
         (get-in basic-plan [:$cycles :$subscription :$begin]))))
