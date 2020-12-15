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

; Bus numbers are all prime
; Schedule shows that buses 601, 41, and 17 arrive at offset 60
; Schedule shows that buses 577 and 29 arrive at offset 29
; LCM of prime numbers is equal to their product

(defn bus-offsets []
  (let [{bus-lines :bus-lines} (read-schedule)
        max-offset (apply max (vals bus-lines))]
    (apply hash-map (mapcat (fn [[bus offset]]
                              (let [offsets (range offset (inc max-offset) bus)]
                                [bus offsets]))
                            bus-lines))))

; NOTE: This is not efficient
(defn part2 []
  (let [{bus-lines :bus-lines} (read-schedule)
        interval (* 601 41 17)]
    (loop [curr (- interval 60)]
      (if (every? (fn [[bus offset]] (= 0 (mod (+ curr offset) bus))) bus-lines)
        curr
        (recur (+ curr interval))))))
