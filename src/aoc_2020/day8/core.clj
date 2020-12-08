(ns aoc-2020.day8.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth]]
    [clojure.set :as set]))

(defn create-instruction [line]
  (let [[op arg] (str/split line #" ")]
    (list op (edn/read-string arg))))

(defn read-instructions []
  (->> (read-file-lines "./src/aoc_2020/day8/input1.txt")
       (map create-instruction)))

(defn execute-instructions [initial-idx instructions loop-cc end-cc]
  (let [instruction-count (count instructions)]
    (loop [idx initial-idx
           accum 0
           ran #{}]
      (if (= idx instruction-count)
        (end-cc accum)
        (let [[op arg] (nth instructions idx)]
          (cond
            (ran idx) (loop-cc accum)
            (= op "nop") (recur (inc idx) accum (conj ran idx))
            (= op "acc") (recur (inc idx) (+ accum arg) (conj ran idx))
            (= op "jmp") (recur (+ idx arg) accum (conj ran idx))
            :else (throw (Throwable. "Invalid operation"))))))))

(defn patch-nop-jmp [instructions idx]
  (if (>= idx (count instructions))
    (throw (Throwable. "Invalid index"))
    (let [[op arg] (nth instructions idx)]
      (cond
        (= op "nop") (replace-nth instructions idx (list "jmp" arg))
        (= op "jmp") (replace-nth instructions idx (list "nop" arg))
        :else instructions))))

(defn fix-instructions [instructions]
  (loop [next-idx 0]
    (let [instruction-patch (patch-nop-jmp instructions next-idx)
          result (execute-instructions 0 instruction-patch (fn [_] nil) (fn [accum] accum))]
      (if (number? result)
        result
        (recur (inc next-idx))))))

(defn part1 []
  (execute-instructions 0 (read-instructions) (fn [accum] accum) nil))

(defn part2 []
  (fix-instructions (read-instructions)))

