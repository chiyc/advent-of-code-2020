(ns aoc-2020.day17.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defrecord Pos [x y z w])

(defn k [pos]
  (str/join "," [(:x pos) (:y pos) (:z pos) (:w pos)]))

(defn build-state [raw-state]
  (let [width (count raw-state)]
    (loop [state {:min (Pos. 0 0 0 0) :max (Pos. width width 0 0)}
           rows raw-state
           row-idx 0]
      (if (empty? rows)
        state
        (let [row-state (map-indexed (fn [i v]
                                       (let [p (Pos. i row-idx 0 0)]
                                         [(k p) v]))
                                     (first rows))]
          (recur (merge state (apply hash-map (flatten row-state)))
                 (rest rows)
                 (inc row-idx)))))))

(defn read-initial-state []
  (build-state (read-file-lines "./src/aoc_2020/day17/input1.txt")))

(defn neighbors [pos wd]
  (let [{x :x
         y :y
         z :z
         w :w} pos]
    (for [xs (range (dec x) (+ x 2))
          ys (range (dec y) (+ y 2))
          zs (range (dec z) (+ z 2))
          ws (range (- w wd) (+ w (inc wd)))
          :when (or (not= xs x) (not= ys y) (not= zs z) (not= ws w))]
      (Pos. xs ys zs ws))))

(defn neighbor-states [pos state w]
  (let [neighbors-pos (neighbors pos w)
        neighbors-ks (map #(k %) neighbors-pos)]
    (map #(get state %) neighbors-ks)))

(defn active? [val]
  (= \# val))

(defn cube [pos state]
  (get state (k pos)))

(defn pos-active? [pos state]
  (active? (cube pos state)))

(defn active-count [neighbor-states]
  (count (filter active? neighbor-states)))

(defn cycle-cube [pos state w]
  (let [neighbor-states (neighbor-states pos state w)
        active-neighbors (active-count neighbor-states)]
    (cond
      (pos-active? pos state) (if (or (= 2 active-neighbors) (= 3 active-neighbors))
                                \#
                                \.)
      :else (if (= 3 active-neighbors)
              \#
              \.))))

(defn next-state [state w]
  (for [xs (range (dec (:x (:min state))) (+ 2 (:x (:max state))))
        ys (range (dec (:y (:min state))) (+ 2 (:y (:max state))))
        zs (range (dec (:z (:min state))) (+ 2 (:z (:max state))))
        ws (range (- (:w (:min state)) w) (+ (inc w) (:w (:max state))))]
    [(k (Pos. xs ys zs ws)) (cycle-cube (Pos. xs ys zs ws) state w)]))

(defn run-cycle
  ([state]
   (run-cycle state 0))
  ([state w]
   (let [next-state (next-state state w)]
     (merge state
            (apply hash-map (flatten next-state))
            {:min (Pos. (dec (:x (:min state)))
                        (dec (:y (:min state)))
                        (dec (:z (:min state)))
                        (- (:w (:min state)) w))
             :max (Pos. (inc (:x (:max state)))
                        (inc (:y (:max state)))
                        (inc (:z (:max state)))
                        (+ w (:w (:max state))))}))))

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

(defn part2 []
  (let [initial-state (read-initial-state)]
    (-> initial-state
        (run-cycle 1)
        (run-cycle 1)
        (run-cycle 1)
        (run-cycle 1)
        (run-cycle 1)
        (run-cycle 1)
        (state-active-count))))
