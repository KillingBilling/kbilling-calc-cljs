(ns kbilling.calc.plans
  (:require [clojure.string]))

(def Decimal (js/require "decimal.js"))
(def reflect-fn (js/require "function-to-string"))

(defn global-var-name [path var-name]
  (let [parts (clojure.string/split var-name #"_")
        cur-cycle (case (path 0)
                    :$cycles (name (path 1))
                    nil)]
    (if (and cur-cycle (= 2 (count parts)))
      (str cur-cycle "_" var-name)
      var-name)))

(defn decimal [x] (if (number? x) (Decimal. x) x))

(extend-type Decimal
  IEquiv
  (-equiv [this other] (.eq this (decimal other))))

(defn args [f] (.-params (reflect-fn f)))

(defn load-fn [f path]
  (let [params (vec (map (comp keyword #(global-var-name path %)) (args f)))
        f (fn [vars]
            (->> params
                 (map (comp decimal #(vars %)))
                 (apply f)
                 decimal))]
    (with-meta f {:params params})))

(defn dec-fn [f]
  (fn [& args] (->> args
                    (map decimal)
                    (apply f)
                    decimal)))

(defn aggregate? [v]
  (and (vector? v) (every? fn? v)))

(defn arity2key [n] (case n
                      2 :$aggr
                      1 :$iden
                      0 :$init))

(defn load-aggr [v]
  (into {} (for [f v] [(arity2key (count (args f))) (dec-fn f)])))

(defn load-v [x path]
  (cond
    (map? x)
    (into {} (for [[k v] x]
               [k (cond
                    (= k :$begin) (set (map keyword v))
                    (= k :$cost) (load-fn v path)
                    (aggregate? v) (load-aggr v)
                    (= path [:$values]) (load-fn v path)
                    (= path [:$notifications]) (load-fn v path)
                    :else (load-v v (conj path k)))]))

    :else x))

(def mod-path (js/require "path"))

(defn root-require [root p]
  (js/require (.resolve mod-path root p)))

(defn mk-load-plan [root]
  (memoize #(load-v (js->clj (root-require root %) :keywordize-keys true) [])))
