(ns kbilling.plans-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [kbilling.plans :as p]
            [cemerick.cljs.test :as t]
            [cljs.nodejs :as node]))


(deftest load-plan-spec
         (is (= '("cycles" "values" "notifications")
                (keys (p/load-plan "test/kbilling/plans/examples/basic"))))
         (is (= ["monthly"]
                (get-in (p/load-plan "test/kbilling/plans/examples/basic") ["cycles" "$subscription" "$begin"]))))


;should PASS
;(deftest somewhat-less-wat
;         (is (= "{}[]" (+ {} []))))

;should PASS
;(deftest javascript-allows-div0
;         (is (= js/Infinity (/ 1 0) (/ (int 1) (int 0)))))

(node/enable-util-print!)
(set! *main-cli-fn* t/run-all-tests)
