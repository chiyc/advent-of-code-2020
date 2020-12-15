(ns aoc-2020.day14.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth expt padl]]
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

(defn mask-value [value mask]
  (let [and-zero-mask (Long/parseLong (str/replace mask "X" "1") 2)
        or-one-mask (Long/parseLong (str/replace mask "X" "0") 2)]
    (bit-or or-one-mask (bit-and and-zero-mask value))))

(defn initialize-program [instructions]
  (loop [memory {}
         [curr & rest] instructions
         mask nil]
    (cond
      (empty? curr) memory
      (:mask curr) (recur memory rest (:mask curr))
      :else (let [address (:address curr)
                  value (:value curr)
                  masked-value (mask-value value mask)]
              (recur (conj memory [address masked-value])
                     rest
                     mask)))))

(defn part1 []
  (->> (initialize-program (read-program-initialization))
       (vals)
       (apply +)))


(defn replace-x [mask x-string]
  (loop [replaced []
         mask (seq mask)
         x-string (seq x-string)]
    (cond
      (empty? mask) (str/replace (str/join replaced) "x" "X")
      (= \0 (first mask)) (recur (conj replaced \x)
                                 (rest mask)
                                 x-string)
      (= \X (first mask)) (recur (conj replaced (first x-string))
                                 (rest mask)
                                 (rest x-string))
      :else (recur (conj replaced (first mask))
                   (rest mask)
                   x-string))))

(defn decode-addresses [address mask]
  (let [x-count (count (filter #(= % \X) mask))
        address-count (expt 2 x-count)]
    (map (fn [x] (let [binary (padl (Long/toBinaryString x) x-count "0")]
                   (mask-value address (replace-x mask binary))))
         (range address-count))))

(defn initialize-program-v2 [instructions]
  (loop [memory {}
         [curr & rest] instructions
         mask nil]
    (cond
      (empty? curr) memory
      (:mask curr) (recur memory rest (:mask curr))
      :else (let [address (:address curr)
                  value (:value curr)
                  addresses (decode-addresses address mask)
                  updated-memory (apply hash-map (mapcat (fn [addr] [addr value]) addresses))]
              (recur (merge memory updated-memory)
                     rest
                     mask)))))

(defn part2 []
  (->> (initialize-program-v2 (read-program-initialization))
       (vals)
       (apply +)))