(ns aoc-2020.core
  (:require
    [aoc-2020.day1.core :as day1]
    [aoc-2020.day2.core :as day2]
    [aoc-2020.day3.core :as day3]
    [aoc-2020.day4.core :as day4]
    [aoc-2020.day5.core :as day5]
    [aoc-2020.day6.core :as day6]
    [aoc-2020.day7.core :as day7]
    [aoc-2020.day8.core :as day8]
    [aoc-2020.day9.core :as day9]))

(defn -main [& args]
  (println "Day 1 - Part 1:" (day1/part1)) ; 1006176
  (println "Day 1 - Part 2:" (day1/part2)) ; 199132160
  (println "Day 2 - Part 1:" (day2/part1)) ; 603
  (println "Day 2 - Part 2:" (day2/part2)) ; 404
  (println "Day 3 - Part 1:" (day3/part1)) ; 223
  (println "Day 3 - Part 2:" (day3/part2)) ; 3517401300
  (println "Day 4 - Part 1:" (day4/part1)) ; 190
  (println "Day 4 - Part 2:" (day4/part2)) ; 121
  (println "Day 5 - Part 1:" (day5/part1)) ; 888
  (println "Day 5 - Part 2:" (day5/part2)) ; 522
  (println "Day 6 - Part 1:" (day6/part1)) ; 6565
  (println "Day 6 - Part 2:" (day6/part2)) ; 3137
  (println "Day 7 - Part 1:" (day7/part1)) ; 316
  (println "Day 7 - Part 2:" (day7/part2)) ; 11310
  (println "Day 8 - Part 1:" (day8/part1)) ; 1586
  (println "Day 8 - Part 2:" (day8/part2)) ; 703
  (println "Day 9 - Part 1:" (day9/part1)) ; 552655238
  (println "Day 9 - Part 2:" (day9/part2)) ;
  )
