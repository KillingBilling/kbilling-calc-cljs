(ns kbilling.plans
  (:require [cljs.nodejs :as node]))

(defn -main [& args]
  (println (apply str (map [\ "world" "hello"] [2 0 1])))
  )


(node/enable-util-print!)
(set! *main-cli-fn* -main)
