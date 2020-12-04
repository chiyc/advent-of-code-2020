(ns aoc-2020.core
  (:require
    [aoc-2020.day1.core :as day1]
    [aoc-2020.day2.core :as day2]
    [aoc-2020.day3.core :as day3]
    [aoc-2020.day4.core :as day4]))

(defn -main [& args]
  (println "Day 1 - Part 1:" (day1/part1)) ; 1006176
  (println "Day 1 - Part 2:" (day1/part2)) ; 199132160
  (println "Day 2 - Part 1:" (day2/part1)) ; 603
  (println "Day 2 - Part 2:" (day2/part2)) ; 404
  (println "Day 3 - Part 1:" (day3/part1)) ; 223
  (println "Day 3 - Part 2:" (day3/part2)) ; 3517401300
  (println "Day 4 - Part 1:" (day4/part1)) ; 190
  (println "Day 4 - Part 2:" (day4/part2)) ;
  )
