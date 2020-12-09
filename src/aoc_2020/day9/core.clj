(ns aoc-2020.day9.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]))

(defn read-input []
  (->> (read-file-lines "./src/aoc_2020/day9/input1.txt")
       (map edn/read-string)))

(defn find-weakness [input]
  (loop [remaining input]
    (let [nums (take 25 remaining)
          sum (nth remaining 25)
          pair-product (get-sum-pair-product sum nums)]
      (if (nil? pair-product)
        sum
        (recur (drop 1 remaining))))))

(defn part1 []
  (find-weakness (read-input)))

(defn part2 []
  )