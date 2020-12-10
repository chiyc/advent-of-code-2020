(ns aoc-2020.day10.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn read-adapter-voltages []
  (->> (read-file-lines "./src/aoc_2020/day10/input1.txt")
       (map edn/read-string)))

(defn device-voltage [voltages]
  (+ 3 (apply max voltages)))

(defn map-voltage-distribution [adapter-voltages]
  (let [device-voltage (device-voltage adapter-voltages)
        voltages (sort (conj adapter-voltages device-voltage))]
    (loop [previous-voltage 0
           voltage-chain voltages
           distribution {1 0, 2 0, 3 0}]
      (if (empty? voltage-chain)
        distribution
        (recur (first voltage-chain)
               (rest voltage-chain)
               (merge-with + distribution (hash-map (- (first voltage-chain) previous-voltage) 1)))))))

(defn part1 []
  (let [distribution (map-voltage-distribution (read-adapter-voltages))]
    (* (distribution 1) (distribution 3))))

(defn is-valid-chain? [adapter-voltages device-voltage]
  (let [sorted-chain (sort adapter-voltages)]
    (loop [previous 0
           chain sorted-chain]
      (cond
        (empty? chain) (<= (- device-voltage previous) 3)
        (< 3 (- (first chain) previous)) false
        :else (recur (first chain) (rest chain))))))

(defn create-1-chain [length]
  (range 3 (+ 3 (inc length))))

(defn valid-1-chains [length]
  (let [base-chain (create-1-chain length)
        device-voltage (+ 3 (apply max base-chain))
        combinations (comb/subsets base-chain)]
    (->> combinations
         (filter #(is-valid-chain? % device-voltage))
         (count))))

(defn list-voltage-differences [adapter-voltages]
  (loop [differences []
         previous 0
         voltages (sort adapter-voltages)]
    (if (empty? voltages)
      differences
      (recur (conj differences (- (first voltages) previous))
             (first voltages)
             (rest voltages)))))

(defn part2 []
  (let [adapter-voltages (read-adapter-voltages)
        differences (list-voltage-differences adapter-voltages)]
    (->> (re-seq #"1+" (apply str differences))
         (map count)
         (reduce (fn [comb curr]
                   (* comb (valid-1-chains curr)))
                 1))))
