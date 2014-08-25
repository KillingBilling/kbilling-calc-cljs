(ns kbilling.plans.test
  (:require [cemerick.cljs.test :as t]
            [cljs.nodejs :as node]
            [kbilling.plans-test]
            [kbilling.plans.transform-test]))

(node/enable-util-print!)
(set! *main-cli-fn* t/run-all-tests)
