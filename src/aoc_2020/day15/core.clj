(ns aoc-2020.day15.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn get-instruction [[instr value]]
  (if (= "mask" instr)
    {:mask value}
    (let [[_ address] (re-matches #"mem\[(\d+)\]" instr)]
      {:address (edn/read-string address)
       :value   (edn/read-string value)})))

(defn read-starting-numbers []
  (-> (read-file "./src/aoc_2020/day15/input1.txt")
      (str/split #",")
      (#(map edn/read-string %))))

(defn play-game [starting-numbers final-turn]
  (loop [turn (inc (count starting-numbers))
         prev-number (last starting-numbers)
         past-numbers (merge (zipmap starting-numbers (range 1 turn))
                             {prev-number nil})]
    (let [prev-turn (dec turn)]
      (if (= prev-turn final-turn)
        prev-number
        (let [prev-number-turn (past-numbers prev-number)
              turn-number (if (number? prev-number-turn) (- prev-turn prev-number-turn) 0)]
          (recur (inc turn) turn-number (merge past-numbers {prev-number prev-turn})))))))

(defn part1 []
  (play-game (read-starting-numbers) 2020))