(ns kbilling.plans.transform)

(def _concat (memoize (fn [a & others] (keyword (apply str (name a) (mapcat (fn [s] [\_ (name s)]) others))))))


(defn or-init [init v] (if v v (init)))
(defn or0 [v] (if v v 0))


(defn add-deltas [vars deltas]
  (merge-with + vars deltas))


(defn aggregate [plan cycles vars buys]
  (into {} (for [[ck c] (:$cycles plan) :when (or (= ck :$subscription) (contains? cycles ck))
                 [acck acc] c :when (contains? buys acck)
                 [aggk agg] acc
                 :let [k (_concat ck acck aggk)]]
             [k ((:$aggr agg) (or-init (:$init agg) (vars k)) (buys acck))])))


(defn calculate-costs [plan cycles cur]
  (into {} (for [[ck c] (:$cycles plan) :when (or (= ck :$subscription) (contains? cycles ck))
                 [acck acc] c :let [cost-fn (:$cost acc)] :when cost-fn]
             [(_concat ck acck :$cost) (cost-fn cur)])))

(defn calculate-values [plan cur])

(defn calculate [plan cycles vars cur]                      ;FIXME incomplete!!!
  (let [cur (merge cur (calculate-costs plan cycles cur))]
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
