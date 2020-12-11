(ns aoc-2020.day11.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn read-seat-layout []
  (->> (read-file-lines "./src/aoc_2020/day11/input1.txt")
       (map #(str/split % #""))))

(defrecord Pos [x y])

(defn square [pos layout]
  (let [row (nth layout (:y pos))]
    (nth row (:x pos))))

(defn occupied? [square]
  (= "#" square))

(defn empty-seat? [square]
  (= "L" square))

(defn floor? [square]
  (= "." square))

(defn get-surrounding [pos layout]
  (let [layout-width (count (first layout))
        layout-height (count layout)
        x (:x pos)
        y (:y pos)
        xmin (max 0 (dec x))
        xmax (min (dec layout-width) (inc x))
        ymin (max 0 (dec y))
        ymax (min (dec layout-height) (inc y))]
    (map #(drop xmin (take (inc xmax) %))
         (drop ymin (take (inc ymax) layout)))))

(defn get-los-seat [pos dir layout]
  (let [height (count layout)
        width (count (first layout))]
    (loop [x (+ (:x pos) (:x dir))
           y (+ (:y pos) (:y dir))]
      (cond
        (or (< y 0) (< x 0) (= y height) (= x width)) nil
        (= "." (square (Pos. x y) layout)) (recur (+ x (:x dir)) (+ y (:y dir)))
        :else (square (Pos. x y) layout)))))

(defn get-line-of-sight [pos layout]
  (list (list (get-los-seat pos (Pos. -1 -1) layout) (get-los-seat pos (Pos. 0 -1) layout) (get-los-seat pos (Pos. 1 -1) layout))
        (list (get-los-seat pos (Pos. -1 0) layout) nil                                  (get-los-seat pos (Pos. 1 0) layout))
        (list (get-los-seat pos (Pos. -1 1) layout) (get-los-seat pos (Pos. 0 1) layout) (get-los-seat pos (Pos. 1 1) layout))))

(defn count-occupied [surroundings]
  (reduce #(+ %1
              (reduce (fn [sum seat] (+ sum (if (occupied? seat) 1 0)))
                      0
                      %2))
          0
          surroundings))

(defn run-round [grid get-neighbors]
  (map-indexed
    #(map-indexed (fn [idx curr]
                    (let [x idx
                          y %1
                          pos (Pos. x y)
                          surrounding (get-neighbors pos grid)]
                      (cond
                        (floor? curr) curr
                        (and (occupied? curr)
                             (> (count-occupied surrounding) 4)) "L"
                        (and (empty-seat? curr)
                             (= (count-occupied surrounding) 0)) "#"
                        :else curr)))
                  %2)
    grid))

(defn same-grid? [grid1 grid2]
  (= (str/join (map #(str/join %) grid1))
     (str/join (map #(str/join %) grid2))))

(defn run-rounds [grid get-neighbors]
  (loop [prev grid
         rounds 0]
    (let [next (run-round prev get-neighbors)]
      (if (and (same-grid? next prev))
        next
        (recur next (inc rounds))))))

(defn part1 []
  (count-occupied (run-rounds (read-seat-layout) get-surrounding)))

(defn part2 []
  (count-occupied (run-rounds (read-seat-layout) get-line-of-sight)))
