(ns aoc-2020.day13.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn read-schedule []
  (let [[min-wait buses] (read-file-lines "./src/aoc_2020/day13/input1.txt")]
    {:min-wait  (edn/read-string min-wait)
     :bus-lines (->> (str/split buses #",")
                     (map-indexed (fn [idx id] [(edn/read-string id) idx]))
                     (filter #(not= 'x (first %)))
                     (flatten)
                     (apply hash-map))}))

(defn part1 []
  (let [{min-wait  :min-wait
         bus-lines :bus-lines} (read-schedule)
        buses (keys bus-lines)
        bus-waits (zipmap buses (map #(- % (mod min-wait %)) buses))]
    (->> (apply min-key second bus-waits)
         (apply *))))


