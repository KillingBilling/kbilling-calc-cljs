(ns kbilling.plans-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [kbilling.plans :as p]
            [cemerick.cljs.test :as t]))


(deftest load-plan
  (is (= ["cycles" "values" "notifications"]
         (keys (p/load-plan "test/kbilling/plans/examples/basic"))))
  (is (= ["monthly"]
         (get-in (p/load-plan "test/kbilling/plans/examples/basic") ["cycles" "$subscription" "$begin"]))))
