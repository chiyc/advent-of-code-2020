(ns aoc-2020.day16.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn parse-rule [line]
  (let [[_ field min1 max1 min2 max2] (re-matches #"([\w ]+): (\d+)-(\d+) or (\d+)-(\d+)" line)]
    {field (fn [val] (let [min1 (edn/read-string min1)
                           max1 (edn/read-string max1)
                           min2 (edn/read-string min2)
                           max2 (edn/read-string max2)]
                       (or (and (>= val min1) (<= val max1))
                           (and (>= val min2) (<= val max2)))))}))

(defn read-ticket-rules []
  (-> (read-file "./src/aoc_2020/day16/input1.txt")
      (str/split #"\n\n")
      (first)
      (str/split #"\n")
      (#(map parse-rule %))
      (#(apply merge %))))

(defn read-input []
  (let [[rules ticket tickets] (-> (read-file "./src/aoc_2020/day16/input1.txt")
                                   (str/split #"\n\n"))]
    {:rules   (->> (str/split-lines rules)
                   (map parse-rule)
                   (apply merge))
     :ticket  (->> (str/split (second (str/split-lines ticket)) #",")
                   (map edn/read-string))
     :tickets (->> (rest (str/split-lines tickets))
                   (map #(str/split % #","))
                   (map #(map edn/read-string %)))}))

(defn invalid? [val rules]
  (not (some (fn [[_ valid?]] (valid? val)) rules)))

(defn possible-ticket? [ticket rules]
  (every? (fn [val] (not (invalid? val rules))) ticket))

(defn get-ticket-error-rate [ticket rules]
  (->> ticket
       (filter #(invalid? % rules))
       (reduce +)))

(defn part1 []
  (let [{tickets :tickets
         rules   :rules} (read-input)]
    (->> tickets
         (map #(get-ticket-error-rate % rules))
         (reduce +))))

(defn part2 []
  (let [{ticket      :ticket
         tickets     :tickets
         rules       :rules} (read-input)
        field-count (count ticket)
        possible-tickets (filter #(possible-ticket? % rules) tickets)
        field-options (map (fn [idx]
                             (let [fields (keys rules)]
                               (filter #(every? (fn [ticket] ((rules %) (nth ticket idx))) possible-tickets)
                                       fields)))
                           (range field-count))
        field-option-sets (apply hash-map (mapcat (fn [fields] [(count fields) (set fields)])
                                                  field-options))
        field-order (map #(first (set/difference (field-option-sets (count %))
                                                 (field-option-sets (dec (count %)))))
                         field-options)]
    (loop [answer 1
           ticket ticket
           field-order field-order]
      (cond
        (empty? ticket) answer
        (str/starts-with? (first field-order) "departure") (recur (* answer (first ticket)) (rest ticket) (rest field-order))
        :else (recur answer (rest ticket) (rest field-order))))))
