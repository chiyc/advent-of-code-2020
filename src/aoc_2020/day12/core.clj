(ns aoc-2020.day12.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn read-directions []
  (->> (read-file-lines "./src/aoc_2020/day12/input1.txt")
       (map (fn [line] {:action (subs line 0 1)
                        :value  (edn/read-string (subs line 1))}))))

; ship is kept as map with keys ;x, :y, :dir

(defn get-new-dir [old-dir deg]
  (let [{x :x
         y :y} old-dir
        rad (- (* deg (/ Math/PI 180)))
        cos (Math/cos rad)
        sin (Math/sin rad)]
    {:x (Math/round (- (* x cos) (* y sin)))
     :y (Math/round (+ (* x sin) (* y cos)))}))

(def ship-actions
  {"N" (fn [{x :x y :y d :dir} val]
         {:x x :y (+ y val) :dir d})
   "S" (fn [{x :x y :y d :dir} val]
         {:x x :y (- y val) :dir d})
   "E" (fn [{x :x y :y d :dir} val]
         {:x (+ x val) :y y :dir d})
   "W" (fn [{x :x y :y d :dir} val]
         {:x (- x val) :y y :dir d})
   "L" (fn [{x :x y :y d :dir} deg]
         {:x x :y y :dir (get-new-dir d (- deg))})
   "R" (fn [{x :x y :y d :dir} deg]
         {:x x :y y :dir (get-new-dir d deg)})
   "F" (fn [{x :x y :y d :dir} val]
         {:x (+ x (* val (d :x))) :y (+ y (* val (d :y))) :dir d})})

(def waypoint-actions
  {"N" (fn [{x :x y :y {wx :x wy :y} :dir} val]
         {:x x :y y :dir {:x wx :y (+ wy val)}})
   "S" (fn [{x :x y :y {wx :x wy :y} :dir} val]
         {:x x :y y :dir {:x wx :y (- wy val)}})
   "E" (fn [{x :x y :y {wx :x wy :y} :dir} val]
         {:x x :y y :dir {:x (+ wx val) :y wy}})
   "W" (fn [{x :x y :y {wx :x wy :y} :dir} val]
         {:x x :y y :dir {:x (- wx val) :y wy}})
   "L" (fn [{x :x y :y d :dir} deg]
         {:x x :y y :dir (get-new-dir d (- deg))})
   "R" (fn [{x :x y :y d :dir} deg]
         {:x x :y y :dir (get-new-dir d deg)})
   "F" (fn [{x :x y :y d :dir} val]
         {:x (+ x (* val (d :x))) :y (+ y (* val (d :y))) :dir d})})

(defn navigate-directions [ship directions actions]
  (loop [ship ship
         [first & rest] directions]
    (println ship first)
    (if (empty? first)
      ship
      (recur ((actions (first :action)) ship (first :value))
             rest))))
(defn part1 []
  (let [directions (read-directions)
        initial-ship {:x 0 :y 0 :dir {:x 1 :y 0}}
        final-ship (navigate-directions initial-ship directions ship-actions)]
    (+ (Math/abs (final-ship :x))
       (Math/abs (final-ship :y)))))

(defn part2 []
  (let [directions (read-directions)
        initial-ship {:x 0 :y 0 :dir {:x 10 :y 1}}
        final-ship (navigate-directions initial-ship directions waypoint-actions)]
    (+ (Math/abs (final-ship :x))
       (Math/abs (final-ship :y)))))
