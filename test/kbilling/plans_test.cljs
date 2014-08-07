(ns kbilling.plans-test
  (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test :as t]
            [cljs.nodejs :as node]))

;should PASS
(deftest somewhat-less-wat
         (is (= "{}[]" (+ {} []))))

;should PASS
(deftest javascript-allows-div0
         (is (= js/Infinity (/ 1 0) (/ (int 1) (int 0)))))

(node/enable-util-print!)
(set! *main-cli-fn* t/run-all-tests)
