(ns kbilling.plans-test
  (:require [cemerick.cljs.test :refer [is deftest with-test run-tests testing test-var]]
            [kbilling.plans :as plans]))

(deftest a-test
         (testing "FIXME, I fail."
                  (is (= 0 1))))

(deftest somewhat-less-wat
         (is (= "{}[]" (+ {} []))))

