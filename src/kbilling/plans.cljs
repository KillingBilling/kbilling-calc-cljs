(ns kbilling.plans)

(def reflect-fn (js/require "function-to-string"))

(defn global-var-name [path var-name]
  (let [parts (clojure.string/split var-name #"\$(?!cost)")
        cur-cycle (case (path 0) :$cycles (name (path 1)) nil)]
    (if (and cur-cycle (= 2 (count parts)))
      (str cur-cycle "$" var-name)
      var-name)))

(defn load-cost-fn [f path]
  (let [params (vec (map (comp keyword #(global-var-name path %)) (.-params (reflect-fn f))))]
    (fn [vars] (apply f (map #(vars %) params)))))

(defn load-v [x path]
  (cond
    (map? x)
    (into {} (for [[k v] x]
               [k (case k
                    :$begin (set (map keyword v))
                    :$cost (load-cost-fn v path)
                    (load-v v (conj path k)))]))

    :else x))

(defn root-require [path]
  (js/require (str (.cwd js/process) "/" path)))

(def load-plan
  (memoize #(load-v (js->clj (root-require %) :keywordize-keys true) [])))
