(ns aoc-2020.day18.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn create-group [line]
  (-> line
      (str/replace #"\s" "")
      (str/split #"")))

(defn read-homework []
  (->> (read-file-lines "./src/aoc_2020/day18/input1.txt")
       (map create-group)))

(def ops {"+" +
          "*" *})

(defn get-paren-group
  ([input]
   (get-paren-group input 1 []))
  ([[first & rest :as all] parens group]
   (cond
     (= 0 parens) [(drop-last group) all]
     (= "(" first) (get-paren-group rest (inc parens) (conj group first))
     (= ")" first) (get-paren-group rest (dec parens) (conj group first))
     :else (get-paren-group rest parens (conj group first)))))

(defn get-plus-group
  ([calculation]
   (get-plus-group calculation 0 []))
  ([[first & rest :as all] parens group]
   (cond
     (empty? first) [group nil]
     (and (= 0 parens)
          (= "*" first)) [group all]
     (= "(" first) (get-plus-group rest (inc parens) (conj group first))
     (= ")" first) (get-plus-group rest (dec parens) (conj group first))
     :else (get-plus-group rest parens (conj group first)))))

(defn calc-group
  ([calculation]
   (calc-group calculation 0 "+" false))
  ([calculation advanced?]
   (calc-group calculation 0 "+" advanced?))
  ([[first & rest :as all] result last-op advanced?]
   ;(println "result " result " last-op " last-op " first " first " rest " (str/join "," rest))
   (cond
     (empty? first) result
     (and (or (= "(" first)
              (number? (edn/read-string first)))
          (= "*" last-op)
          advanced?) (let [[group after] (get-plus-group all)]
                       (calc-group after
                                   ((ops last-op) result (calc-group group advanced?))
                                   last-op
                                   advanced?))
     (= "(" first) (let [[group after] (get-paren-group rest)]
                     (calc-group after
                                 ((ops last-op) result (calc-group group advanced?))
                                 last-op
                                 advanced?))
     (number? (edn/read-string first)) (calc-group rest
                                                   ((ops last-op) result (edn/read-string first))
                                                   last-op
                                                   advanced?)
     :else (calc-group rest
                       result
                       first
                       advanced?))))

(defn part1 []
  (->> (read-homework)
       (map calc-group)
       (reduce +)))

(defn part2 []
  (->> (read-homework)
       (map #(calc-group % true))
       (reduce +)))
