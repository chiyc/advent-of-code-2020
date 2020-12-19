(ns aoc-2020.day19.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn read-input []
  (-> (read-file "./src/aoc_2020/day19/input1.txt")
      (str/split #"\n\n")))


(defn create-rule [line]
  (let [[rule-id rule] (str/split line #": ")
        char-match (re-matches #"\"([a-z])\"" rule)]
    (if char-match
      [rule-id (second char-match)]
      [rule-id (map #(str/split % #" ")
                    (str/split rule #" \| "))])))

(defn get-rules [input]
  (let [[raw-rules] input]
    (->> (str/split-lines raw-rules)
         (mapcat create-rule)
         (apply hash-map))))

(defn get-messages [input]
  (let [[_ raw-messages] input]
    (str/split-lines raw-messages)))

(defn execute-rules [rule-ids s rules])

(defn execute-rule [rule-id s rules]
  (let [rule (rules rule-id)]
    (cond
      (= "" s) (empty? rule-id)
      (empty? rule-id) s
      (string? rule) (when (str/starts-with? s rule)
                       (subs s 1))
      :else (some (fn [possibility] (execute-rules possibility s rules))
                  rule))))

(defn execute-rules [rule-ids s rules]
  (let [[first & rest] rule-ids]
    (cond
      (= "" s) (empty? first)
      (empty? first) s
      :else (let [next (execute-rule first s rules)]
              (if (string? next)
                (execute-rules rest next rules)
                next)))))

(defn part1 []
  (let [input (read-input)
        rules (get-rules input)
        messages (get-messages input)]
    (->> messages
         (map #(execute-rule "0" % rules))
         (filter #(= true %))
         (count))))

(defn part2 []
  (let [input (read-input)
        rules (conj (get-rules input)
                    (create-rule "8: 42 | 42 8")
                    (create-rule "11: 42 31 | 42 11 31"))
        messages (get-messages input)]
    (->> messages
         (map #(execute-rule "0" % rules))
         ;(map #(list % (execute-rule "0" % rules)))
         (filter #(= true %))
         (count)
         )))
