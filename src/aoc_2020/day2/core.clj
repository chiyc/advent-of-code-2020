(ns aoc-2020.day2.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file]]))

(defn count-char [c password]
  (->> (str/split password #"")
       (filter #(= c %))
       (count)))

(defn password-valid? [record]
  (let [c        (nth record 2)
        min      (edn/read-string (nth record 0))
        max      (edn/read-string (nth record 1))
        password (nth record 3)
        occurs   (count-char c password)]
    (and (>= occurs min)
         (<= occurs max))))

(defn read-input1 []
  (->> (read-file "./src/aoc_2020/day2/input1.txt")
       (map #(str/split % #"[- :]+"))))

(defn part1 []
  (->> (read-input1)
       (filter #(password-valid? %))
       (count)))
