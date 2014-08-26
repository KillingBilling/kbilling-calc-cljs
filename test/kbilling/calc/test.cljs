(ns kbilling.calc.test
  (:require [cemerick.cljs.test :as t]
            [cljs.nodejs :as node]
            [kbilling.calc.plans-test]
            [kbilling.calc.transform-test]))

(node/enable-util-print!)
(set! *main-cli-fn* #(t/run-all-tests))
