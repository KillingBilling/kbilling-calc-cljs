(ns kbilling.calc.main-test
  (:require-macros [cemerick.cljs.test :refer [is deftest with-test run-tests testing test-var]])
  (:require [kbilling.calc.main :as main]
            [cemerick.cljs.test :as t]))

(deftest transform-dummy-test
  (is (= (main/transform "qwerty") "QWERTY")))
