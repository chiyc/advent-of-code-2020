(ns aoc-2020.day1.core
  (:require
    [clojure.edn :as edn]
    [aoc-2020.util :refer [read-file-lines]]))

(defn get-num-list []
  (->> (read-file-lines "./src/aoc_2020/day1/input1.txt")
       (map edn/read-string)))

(defn get-sum-pair-product [sum nums]
  (let [num-set (set nums)]
    (loop [[first & rest] nums]
      (cond
        (nil? first) nil
        :else (let [comp (- sum first)]
                (if (contains? num-set comp) (* comp first)
                                             (recur rest)))))))

(defn get-sum-triple-product [sum nums]
  (loop [[first & rest] nums]
    (let [comp (- sum first)
          pair-product (get-sum-pair-product comp nums)]
      (if (not (nil? pair-product))
        (* first pair-product)
        (recur rest)))))

(defn part1 []
  (let [nums (get-num-list)]
   (get-sum-pair-product 2020
                         nums)))

(defn part2 []
  (let [nums (get-num-list)]
    (get-sum-triple-product 2020
                            nums)))
