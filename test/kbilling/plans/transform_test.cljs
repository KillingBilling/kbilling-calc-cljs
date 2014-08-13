(ns kbilling.plans.transform-test
  (:require-macros [cemerick.cljs.test
                    :refer [is deftest with-test run-tests testing test-var]])
  (:require [kbilling.plans :as p]
            [kbilling.plans.transform :as tf]
            [cemerick.cljs.test :as t]
            [cljs.nodejs :as node]))

(def BigNumber (js/require "bignumber.js"))

(deftest big-number-construction
  (is (= {:coverage "200"
          :rub      "60"}
         {:coverage (str (BigNumber. 200))
          :rub      (str (.times (BigNumber. 200) (BigNumber. 0.3)))})))

(def basic-plan (p/load-plan "test/kbilling/plans/examples/basic"))

(deftest aggregate
  (is (= {"monthly$coverage$sum" "200"}
         (tf/aggregate basic-plan #{"monthly"} {} {"coverage" 200}))))