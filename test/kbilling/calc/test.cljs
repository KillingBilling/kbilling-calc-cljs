(ns kbilling.calc.test
  (:require [cljs.test :as t :include-macros true]
            [cljs.nodejs :as node]
            [kbilling.calc.plans-test]
            [kbilling.calc.transform-test]
            [kbilling.calc.main-test]))

(node/enable-util-print!)
(set! *main-cli-fn* #(t/run-all-tests))
