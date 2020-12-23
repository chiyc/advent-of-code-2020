(ns aoc-2020.day23.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file index-of]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn read-input
  ([]
   (read-input "./src/aoc_2020/day23/input1.txt"))
  ([file] :grid
   (->> (read-file file)
        (seq)
        (map #(edn/read-string (str %))))))

(defn pick-next-three [index cups]
  (loop [picked []
         index index]
    (if (= 3 (count picked))
      picked
      (let [next-index (mod (inc index) (count cups))]
        (recur (conj picked (nth cups next-index))
               next-index)))))

(defn get-destination [current-cup picked-up cups]
  (let [min-cup (apply min cups)
        max-cup (apply max cups)]
    (loop [destination (if (< (dec current-cup) min-cup)
                         max-cup
                         (dec current-cup))]
      (let [picked-up? (index-of destination picked-up)]
        (if (not picked-up?)
          destination
          (recur (if (= destination min-cup)
                   max-cup
                   (dec destination))))))))

(defn move-picked-up [picked-up destination cups]
  (let [picked-up-removed (remove (set picked-up) cups)
        destination-index (index-of destination picked-up-removed)]
    (concat (take (inc destination-index) picked-up-removed)
            picked-up
            (drop (inc destination-index) picked-up-removed))))

(defn play-game [cups turns]
  (loop [turns-played 0
         current-cup (first cups)
         cups cups]
    (if (= turns-played turns)
      cups
      (let [current-cup-index (index-of current-cup cups)
            picked-up (pick-next-three current-cup-index cups)
            destination (get-destination current-cup picked-up cups)
            next-cups (move-picked-up picked-up destination cups)
            next-current (nth next-cups (mod (inc (index-of current-cup next-cups)) (count next-cups)))]
        (recur (inc turns-played)
               next-current
               next-cups)))))

(defn part1 []
  (let [cups (read-input)
        game-end (play-game cups 100)
        one-index (index-of 1 game-end)]
    (apply str (concat (drop (inc one-index) game-end)
                       (take one-index game-end)))))
