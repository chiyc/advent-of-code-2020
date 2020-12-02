(ns aoc-2020.core
  (:require
    [aoc-2020.day1.core :as day1]
    [aoc-2020.day2.core :as day2]))

(defn -main [& args]
  (println "Day 1 - Part 1:" (day1/part1)) ; 1006176
  (println "Day 1 - Part 2:" (day1/part2)) ; 199132160
  (println "Day 2 - Part 1:" (day2/part1)) ; 603
  )
