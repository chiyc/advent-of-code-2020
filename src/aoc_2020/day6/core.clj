(ns aoc-2020.day6.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file]]
    [clojure.set :as set]))

(defn read-group-inputs []
  (-> (read-file "./src/aoc_2020/day6/input1.txt")
      (str/split #"\n\n")))

(defn get-individual-response [individual-input]
  (->> (str/split individual-input #"")
       (mapcat #(vector % 1))
       (apply hash-map)))

(defn get-group-response [group-input]
  (->> (str/split-lines group-input)
       (map get-individual-response)
       (apply merge-with +)))

(defn count-group-members [group-input]
  (count (str/split-lines group-input)))

(defn count-group-yeses [group-response]
  (count (keys group-response)))

(defn count-unanimous-yeses [group-input]
  (let [members (count-group-members group-input)
        group-response (get-group-response group-input)]
    (-> (filter (comp #{members} last) group-response)
        (count))))

(defn count-all-yeses []
  (->> (read-group-inputs)
       (map get-group-response)
       (map count-group-yeses)
       (reduce +)))

(defn count-all-unanimous-yeses []
  (->> (read-group-inputs)
       (map count-unanimous-yeses)
       (reduce +)))

(defn part1 []
  (count-all-yeses))

(defn part2 []
  (count-all-unanimous-yeses))
