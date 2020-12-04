(ns aoc-2020.day3.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines]]))

(defrecord Position [x y])

(defn read-input1 []
  (->> (read-file-lines "./src/aoc_2020/day3/input1.txt")
       (map #(str/split % #""))))

(defn move-on-grid [grid pos mov]
  (let [width  (count (first grid))
        height (count grid)
        new-x  (mod (+ (:x pos) (:x mov))
                   width)
        new-y  (+ (:y pos) (:y mov))]
    (if (>= new-y height)
        nil
        (Position. new-x new-y))))

(defn grid-square [grid pos]
  (let [row (nth grid (:y pos))]
    (nth row (:x pos))))

(defn on-tree? [grid pos]
  (= "#" (grid-square grid pos)))

(defn count-arboreal-stops [slope]
  (let [grid        (read-input1)
        initial-pos (Position. 0 0)]
    (loop [tree-count 0
           position   initial-pos]
      (let [next-position (move-on-grid grid position slope)]
        (cond
          (nil? next-position)          tree-count
          (on-tree? grid next-position) (recur (inc tree-count) next-position)
          :else                         (recur tree-count next-position))))))

(defn part1 []
  (count-arboreal-stops (Position. 3 1)))

(defn part2 []
  (let [slopes (list (Position. 1 1)
                     (Position. 3 1)
                     (Position. 5 1)
                     (Position. 7 1)
                     (Position. 1 2))]
    (reduce #(* %1 (count-arboreal-stops %2)) 1 slopes)))
