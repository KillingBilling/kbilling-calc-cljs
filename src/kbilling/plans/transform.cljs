(ns kbilling.plans.transform)

(defn $ [a & others] (apply str a (mapcat (fn [s] [\$ s]) others)))


(defn or-init [init v] (if v v (init)))


(defn add-deltas [vars deltas]
  (merge-with + vars deltas))


(defn aggregate [plan cycles vars buys]
  (into {} (for [[ck c] (plan "cycles") :when (or (= ck "$subscription") (contains? cycles ck))
                 [acck acc] c :when (contains? buys acck)
                 [aggk agg] acc
                 :let [k ($ ck acck aggk)]]
             [k ((agg "aggr") (or-init (agg "init") (vars k)) (buys acck))])))


(defn calculate-no-values [plan cycles vars cur]
  (into {} (for [[ck c] (plan "cycles") :when (or (= ck "$subscription") (contains? cycles ck))
                 [acck acc] c :let [cost (acc "$cost")]]
             '???)))

(defn calculate-values [plan cur])

(defn calculate [plan cycles vars cur]
  (let [cur (merge cur (calculate-no-values plan cycles vars cur))]
    (merge cur (calculate-values plan cur))))


(defn apply-add-buy [plan cycles vars adds buys]
  (let [cur (-> vars
                (add-deltas adds)
                (add-deltas buys)
                (merge (aggregate plan cycles vars buys)))]
    (merge cur (calculate plan cycles vars cur))))

(defn subscribe [plan cycles vars])

(defn cycle-begin [plan cycles vars cycle])

(defn transform [inobj])
