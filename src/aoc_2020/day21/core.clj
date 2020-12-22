(ns aoc-2020.day21.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn read-input
  ([]
   (read-input "./src/aoc_2020/day21/input1.txt"))
  ([file] :grid
   (->> (read-file-lines file)
        (map (fn [line]
               (let [[_ allergens] (re-find #"\(contains ([\w, ]+)\)$" line)
                     [ingredients] (str/split line #" \(contains")]
                 {:ingredients (set (str/split ingredients #" "))
                  :allergens   (set (str/split allergens #", "))}))))))

(defn part1 []
  (let [foods (read-input)
        allergens (->> foods
                       (mapcat :allergens)
                       set
                       seq)
        possible-allergens (->> allergens
                                (mapcat (fn [allergen]
                                          (->> foods
                                               (filter #(contains? (% :allergens) allergen))
                                               (map :ingredients)
                                               (apply set/intersection)
                                               (#(vector allergen %)))))
                                (apply hash-map))
        allergen-ingredients
        (loop [allergen-ingredients (apply hash-map (mapcat (fn [[allergen ingredient-set]] [allergen (first ingredient-set)])
                                                            (filter #(= 1 (count (second %)))
                                                                    possible-allergens)))
               allergen-options (filter #(not= 1 (count (second %))) possible-allergens)]
          (if (empty? allergen-options)
            allergen-ingredients
            (let [known-allergens (set (vals allergen-ingredients))
                  next-options (map #(vector (first %) (set/difference (second %) known-allergens))
                                    allergen-options)]
              (println "next" next-options)
              (recur (merge allergen-ingredients (apply hash-map
                                                        (mapcat (fn [[allergen ingredient-set]] [allergen (first ingredient-set)])
                                                                (filter #(= 1 (count (second %)))
                                                                        next-options))))
                     (filter #(not= 1 (count (second %))) next-options)))))]
    (->> foods
         (map #(set/difference (% :ingredients) (set (vals allergen-ingredients))))
         (map count)
         (apply +))))


(println (part1))
