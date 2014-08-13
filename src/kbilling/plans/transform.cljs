(ns kbilling.plans.transform)

(defn $ [a & others] (apply str a (mapcat (fn [s] [\$ s]) others)))


(defn or-init [agg vars k] 0)                               ;TODO incomplete!!!


(defn add-deltas [vars deltas]
  (merge-with + vars deltas))


(defn aggregate [plan cycles vars buys]
  (apply array-map
         (flatten (for [[ck c] (plan "cycles") :when (or (= ck "$subscription") (contains? cycles ck))
                        [acck acc] c :when (contains? buys acck)
                        [aggk agg] acc]
                    [($ ck acck aggk) ((agg "aggr") (or-init agg vars ($ ck acck aggk)) (buys acck))]))))


(defn calculate [plan cycles cur])


(defn apply-add-buy [plan cycles vars adds buys]
  (let [cur (-> vars
                (add-deltas adds)
                (add-deltas buys)
                (merge (aggregate plan cycles vars buys)))]
    (merge cur (calculate plan cycles cur))))

(defn subscribe [plan cycles vars])

(defn cycle-begin [plan cycles vars cycle])

(defn transform [inobj])
