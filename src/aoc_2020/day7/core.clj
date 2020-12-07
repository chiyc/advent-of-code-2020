(ns aoc-2020.day7.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines]]
    [clojure.set :as set]))

(defn create-rule [input]
  (let [[primary-bag contents] (str/split input #" bags contain ")
        contents (str/split contents #" bags?[\.,]?\s?")
        no-contents (= "no other" (first contents))]
    (hash-map primary-bag
              (if no-contents
                {}
                (apply merge (map #(let [[_ quantity color] (re-matches #"(\d+) (.+)" %)]
                                     {color (edn/read-string quantity)})
                                  contents))))))

(defn read-rules []
  (->> (read-file-lines "./src/aoc_2020/day7/input1.txt")
       (map create-rule)
       (apply merge)))

(defn bag-contains-color? [outer-color search-color rules]
  (let [contents (rules outer-color)]
    (if (= 0 (count contents))
      false
      (or (contents search-color)
          (some (fn [[color _]] (bag-contains-color? color search-color rules))
                contents)))))

(defn part1 []
  (let [rules (read-rules)]
    (-> (filter (fn [[bag _]] (bag-contains-color? bag "shiny gold" rules))
                rules)
        (count))))

(defn count-inner-bags [outer-color rules]
  (let [contents (rules outer-color)]
    (if (= 0 (count contents))
      0
      (reduce (fn [sum [color quantity]]
                (+ sum
                   quantity
                   (* quantity (count-inner-bags color rules))))
              0
              contents))))

(defn part2 []
  (let [rules (read-rules)]
    (count-inner-bags "shiny gold" rules)))
