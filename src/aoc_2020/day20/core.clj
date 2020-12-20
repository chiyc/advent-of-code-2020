(ns aoc-2020.day20.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file-lines replace-nth read-file]]
    [clojure.set :as set]
    [aoc-2020.day1.core :refer [get-sum-pair-product]]
    [clojure.math.combinatorics :as comb]))

(defn read-input
  ([]
   (read-input "./src/aoc_2020/day20/input1.txt"))
  ([file] :grid
   (-> (read-file file)
       (str/split #"\n\n"))))

(defn get-top-border [grid]
  (first grid))

(defn get-bottom-border [grid]
  (str/join (reverse (last grid))))

(defn get-left-border [grid]
  (str/join (reverse (map #(get % 0) grid))))

(defn get-right-border [grid]
  (let [width (count grid)]
    (str/join (map #(get % (dec width)) grid))))

(defn get-borders [grid]
  (list (get-top-border grid)
        (get-right-border grid)
        (get-bottom-border grid)
        (get-left-border grid)))

(defn create-tile [raw-input]
  (let [[id-line & grid] (str/split-lines raw-input)
        [_ raw-id] (re-matches #"Tile (\d+):" id-line)
        id (edn/read-string raw-id)]
    [id {:id      id
         :grid    grid
         :borders (get-borders grid)
         :matches (list nil nil nil nil)
         :flipped nil}]))

(defn get-tiles []
  (->> (read-input)
       (mapcat create-tile)
       (apply hash-map)))

(defn get-border-map [tiles]
  (apply merge-with concat
         (map (fn [[id tile]]
                (let [borders (tile :borders)]
                  (apply merge-with concat
                         (map #(hash-map % (list (tile :id))) borders))))
              tiles)))

(defn get-border-matches [border borders]
  (let [match-border (str/join (reverse border))]
    (borders match-border)))

(defn border-match-count [tile borders]
  (let [tile-borders (tile :borders)]
    (->> tile-borders
         (map #()))))

(defn part1 []
  (let [tiles (get-tiles)
        borders (get-border-map tiles)]
    (->> tiles
         (filter (fn [[id tile]]
                   (let [tile-borders (tile :borders)]
                     (= 0 (border-match-count tile borders)))))
         ;(map (fn [tile] {:id (tile :id) :borders (tile :borders)}))
         (first)
         )
    #_(->> borders
           (filter (fn [[k v]] (not= (count v) (count (set v)))))
           )))

; EXPECT
; 576 borders
; expect 48 without pairs
; expect 264 paired border patterns
; expect 312 unique borders

; ACTUAL
; borders map does sum to 576
; each of the four borders on each tile are unique to that tile
