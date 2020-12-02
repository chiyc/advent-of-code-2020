(ns aoc-2020.day2.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file]]))

(defn count-char [c password]
  (->> (str/split password #"")
       (filter #(= c %))
       (count)))

(defn policy1-password-valid? [record]
  (let [c        (nth record 2)
        min      (edn/read-string (nth record 0))
        max      (edn/read-string (nth record 1))
        password (nth record 3)
        occurs   (count-char c password)]
    (and (>= occurs min)
         (<= occurs max))))

(defn occurs-once? [c idx1 idx2 password]
  (let [c1 (subs password idx1 (inc idx1))
        c2 (subs password idx2 (inc idx2))]
    (and (not= c1 c2)
         (or (= c c1)
             (= c c2)))))

(defn policy2-password-valid? [record]
  (let [c        (nth record 2)
        pos1     (edn/read-string (nth record 0))
        pos2     (edn/read-string (nth record 1))
        password (nth record 3)]
    (occurs-once? c (dec pos1) (dec pos2) password)))

(defn read-input1 []
  (->> (read-file "./src/aoc_2020/day2/input1.txt")
       (map #(str/split % #"[- :]+"))))

(defn part1 []
  (->> (read-input1)
       (filter #(policy1-password-valid? %))
       (count)))

(defn part2 []
  (->> (read-input1)
       (filter #(policy2-password-valid? %))
       (count)))
