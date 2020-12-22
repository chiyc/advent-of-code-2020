(ns aoc-2020.day22.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn get-decks [[p1-input p2-input]]
  [(map edn/read-string (rest (str/split-lines p1-input)))
   (map edn/read-string (rest (str/split-lines p2-input)))])

(defn read-input
  ([]
   (read-input "./src/aoc_2020/day22/input1.txt"))
  ([file] :grid
   (-> (read-file file)
       (str/split #"\n\n")
       (get-decks))))

(defn play-game [decks]
  (loop [[p1-deck p2-deck] decks]
    (println p1-deck p2-deck)
    (if (or (empty? p1-deck) (empty? p2-deck))
      [p1-deck p2-deck]
      (let [[p1-first & p1-rest] p1-deck
            [p2-first & p2-rest] p2-deck]
        (cond
          (> p1-first p2-first) (recur [(concat p1-rest (list p1-first p2-first))
                                        p2-rest])
          (> p2-first p1-first) (recur [p1-rest
                                        (concat p2-rest (list p2-first p1-first))])
          :else (throw (Throwable. "Tied round")))))))

(defn count-score [winning-deck]
  (->> (reverse winning-deck)
       (map-indexed (fn [idx card] (* (inc idx) card)))
       (reduce +)))

(defn part1 []
  (let [initial-decks (read-input)
        end-game (play-game initial-decks)]
    (if (empty? (first end-game))
      (count-score (second end-game))
      (count-score (first end-game)))))
