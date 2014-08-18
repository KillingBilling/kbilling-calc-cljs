(ns kbilling.plans.transform)

(def BigNumber (js/require "bignumber.js"))


(def _concat (memoize (fn [a & others] (keyword (apply str (name a) (for [s others, _ [\_ (name s)]] _))))))


(defn or-init [init v] (if v v (init)))
(defn or0 [v] (if v v 0))


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

(defn +bign [x y] (.plus (BigNumber (or0 x)) (BigNumber (or0 y))))
(defn -bign [x y] (.minus (BigNumber (or0 x)) (BigNumber (or0 y))))

(defn add-deltas [vars deltas] (merge-with +bign vars deltas))

(defn apply-costs [vars costs]
  (let [cost-deltas (for [[costk costv] costs
                          :let [costv-prev (or0 (costk vars))
                                cost-delta (.minus (BigNumber costv) (BigNumber costv-prev))
                                acck (keyword ((clojure.string/split costk #"_") 1))]]
                      [acck cost-delta])]
    (into {} (->> cost-deltas
                  (group-by #(% 0))
                  (map (fn [[acck costs]] [acck (->> costs (map #(% 1)) (reduce +bign 0))]))
                  (map (fn [[acck delta]] [acck (-bign (acck vars) delta)]))))))

(defn calculate-values [plan cur]
  (into {} (for [[val-k val-fn] (or (:$values plan) [])]
             [val-k (val-fn cur)])))

(defn calculate-no-values [plan cycles vars cur]
  (let [costs  (calculate-costs plan cycles cur)]
    (merge costs (apply-costs vars costs))))

(defn calculate [plan cycles vars cur]
  (let [cur (merge vars (calculate-no-values plan cycles vars cur))]
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
