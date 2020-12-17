(ns aoc-2020.day17.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defrecord Pos [x y z])

(defn coord [key]
  (let [[x y z] (str/split key #",")]
    (Pos. x y z)))

(defn k [pos]
  (str/join "," [(:x pos) (:y pos) (:z pos)]))

(defn build-state [raw-state]
  (let [width (count raw-state)]
    (loop [state {:min (Pos. 0 0 0) :max (Pos. width width 0)}
           rows raw-state
           row-idx 0]
      (if (empty? rows)
        state
        (let [row-state (map-indexed (fn [i v]
                                       (let [p (Pos. i row-idx 0)]
                                         [(k p) v]))
                                     (first rows))]
          (recur (merge state (apply hash-map (flatten row-state)))
                 (rest rows)
                 (inc row-idx)))))))

(defn read-initial-state []
  (build-state (read-file-lines "./src/aoc_2020/day17/input1.txt")))

(defn neighbors [pos]
  (let [{x :x
         y :y
         z :z} pos]
    (for [xs (range (dec x) (+ x 2))
          ys (range (dec y) (+ y 2))
          zs (range (dec z) (+ z 2))
          :when (or (not= xs x) (not= ys y) (not= zs z))]
      (Pos. xs ys zs))))

(defn neighbor-states [pos state]
  (let [neighbors-pos (neighbors pos)
        neighbors-ks (map #(k %) neighbors-pos)]
    (map #(get state %) neighbors-ks)))

(defn inactive? [val]
  (not= \# val))

(defn active? [val]
  (= \# val))

(defn cube [pos state]
  (get state (k pos)))

(defn pos-inactive? [pos state]
  (inactive? (cube pos state)))

(defn pos-active? [pos state]
  (active? (cube pos state)))

(defn active-count [neighbor-states]
  (count (filter active? neighbor-states)))

(defn inactive-count [neighbor-states]
  (count (filter inactive? neighbor-states)))

(defn cycle-cube [pos state]
  (let [neighbor-states (neighbor-states pos state)
        active-neighbors (active-count neighbor-states)]
    (cond
      (pos-active? pos state) (if (or (= 2 active-neighbors) (= 3 active-neighbors))
                                \#
                                \.)
      :else (if (= 3 active-neighbors)
              \#
              \.))))

(defn cycle-row [])

(defn next-state [state]
  (for [xs (range (dec (:x (:min state))) (+ 2 (:x (:max state))))
        ys (range (dec (:y (:min state))) (+ 2 (:y (:max state))))
        zs (range (dec (:z (:min state))) (+ 2 (:z (:max state))))]
    [(k (Pos. xs ys zs)) (cycle-cube (Pos. xs ys zs) state)]))

(defn run-cycle [state]
  (let [next-state (next-state state)]
    (merge state
           (apply hash-map (flatten next-state))
           {:min (Pos. (dec (:x (:min state)))
                       (dec (:y (:min state)))
                       (dec (:z (:min state))))
            :max (Pos. (inc (:x (:max state)))
                       (inc (:y (:max state)))
                       (inc (:z (:max state))))})))

(defn state-active-count [state]
  (count (filter active? (vals state))))

(defn part1 []
  (let [initial-state (read-initial-state)]
    (-> initial-state
        (run-cycle)
        (run-cycle)
        (run-cycle)
        (run-cycle)
        (run-cycle)
        (run-cycle)
        (state-active-count))))
