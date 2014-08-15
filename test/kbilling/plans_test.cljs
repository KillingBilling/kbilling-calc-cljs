(ns kbilling.plans-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [kbilling.plans :as p]
            [cemerick.cljs.test :as t]))

(def reflect-fn (js/require "function-to-string"))


(def basic-plan (p/load-plan "test/kbilling/plans/examples/basic"))

(deftest load-cost-fn-test
  (let [f (js/eval "(function(coverage$sum) { return coverage$sum * 0.3 })")
        cost-fn (p/load-cost-fn f [])]
    (is (= 60 (cost-fn {:coverage$sum 200})))))

#_(deftest cost-function
  (let [cost (get-in basic-plan [:cycles :monthly :rub :$cost])]
    (is (= "60" (str (cost {"coverage$sum" 200}))))))

(deftest load-plan
  (is (= [:$cycles :$values :$notifications]
         (keys basic-plan)))
  (is (= #{:monthly}
         (get-in basic-plan [:$cycles :$subscription :$begin]))))
