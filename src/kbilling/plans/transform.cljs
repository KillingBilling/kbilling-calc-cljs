(ns kbilling.plans.transform)



(defn add-deltas [vars deltas]
  (merge-with + vars deltas))


(defn aggregate [plan cycles vars buys])


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
