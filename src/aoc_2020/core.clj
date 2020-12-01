(ns aoc-2020.core
  (:require
    [aoc-2020.day1.core :as day1]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(foo "Chi'")

(defn -main [& args]
  (println (day1/part1)) ; 1006176
  (println (day1/part2))) ; 199132160?
