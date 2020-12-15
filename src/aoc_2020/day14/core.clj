(ns aoc-2020.day14.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn get-instruction [[instr value]]
  (if (= "mask" instr)
    {:mask value}
    (let [[_ address] (re-matches #"mem\[(\d+)\]" instr)]
      {:address (edn/read-string address)
       :value   (edn/read-string value)})))

(defn read-program-initialization []
  (->> (read-file-lines "./src/aoc_2020/day14/input1.txt")
       (map #(str/split % #" = "))
       (map get-instruction)))

; and 0, or 1



(defn initialize-program [instructions]
  (loop [memory {}
         [curr & rest] instructions
         mask nil]
    (cond
      (empty? curr) memory
      (:mask curr) (recur memory rest (:mask curr))
      :else (let [and-zero-mask (Long/parseLong (str/replace mask "X" "1") 2)
                  or-one-mask (Long/parseLong (str/replace mask "X" "0") 2)
                  address (:address curr)
                  value (:value curr)
                  masked-value (bit-or or-one-mask (bit-and and-zero-mask value))]
              (recur (conj memory [address masked-value])
                     rest
                     mask)))))

(defn part1 []
  (->> (initialize-program (read-program-initialization))
       (vals)
       (apply +)))

(defn initialize-program-v2 [instructions]
  (loop [memory {}
         [curr & rest] instructions
         mask nil]
    (cond
      (empty? curr) memory
      (:mask curr) (recur memory rest (:mask curr))
      :else (let [and-zero-mask (Long/parseLong (str/replace mask "X" "1") 2)
                  or-one-mask (Long/parseLong (str/replace mask "X" "0") 2)
                  address (:address curr)
                  value (:value curr)
                  masked-value (bit-or or-one-mask (bit-and and-zero-mask value))]
              (recur (conj memory [address masked-value])
                     rest
                     mask)))))

(defn part2 []
  )