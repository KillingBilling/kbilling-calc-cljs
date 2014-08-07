(ns kbilling.plans.test
  (:require [cemerick.cljs.test :as t]
            [cljs.nodejs :as node]))


(node/enable-util-print!)
(set! *main-cli-fn* t/run-all-tests)
