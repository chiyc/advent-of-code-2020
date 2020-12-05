(ns aoc-2020.day5.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines]]
    [clojure.set :as set]))

(defn read-seats []
  (read-file-lines "./src/aoc_2020/day5/input1.txt"))

(defn expt [base pow]
  (loop [exponent pow
         curr 1]
    (if (= exponent 0)
      curr
      (recur (dec exponent) (* base curr)))))

(defrecord Range [min max])

; Range List-of-partition Lower-partition-marker -> Range
;   partitions is a string of partition markers like "FBBFFBF"
;   lower-half-marker marks to bisect on the lower half of the range
(defn bisect-space [range partitions lower-half-marker]
  (reduce #(let [min (:min %1)
                 max (:max %1)
                 half-range (quot (- max min) 2)]
             (if (= %2 lower-half-marker)
               (Range. min (+ min half-range))
               (Range. (- max half-range) max)))
          range
          partitions))

(defn get-seat-row [seat]
  (let [row-partition (re-find #"[FB]+" seat)
        max-row (dec (expt 2 (count row-partition)))
        range (Range. 0 max-row)]
    (-> (bisect-space range row-partition \F)
        (:min))))

(defn get-seat-col [seat]
  (let [col-partition (re-find #"[LR]+" seat)
        max-col (dec (expt 2 (count col-partition)))
        range (Range. 0 max-col)]
    (-> (bisect-space range col-partition \L)
        (:min))))

(defn get-seat-id [seat]
  (+ (* (get-seat-row seat)
        8)
     (get-seat-col seat)))

(defn get-seat-ids []
  (map get-seat-id (read-seats)))

(defn part1 []
  (apply max (get-seat-ids)))

(defn part2 []
  (let [seat-ids (set (get-seat-ids))
        min-seat-id (apply min seat-ids)
        max-seat-id (apply max seat-ids)
        complete-seat-ids (set (range min-seat-id (inc max-seat-id)))]
    (set/difference complete-seat-ids seat-ids)))
