(ns aoc-2020.util
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]))

(defn read-file-lines [filename]
  (->> (io/file filename)
       slurp
       str/split-lines
       (map str/trim)))

(defn read-file [filename]
  (->> (io/file filename)
       slurp))

(defn replace-nth [coll idx new]
  (concat (take idx coll) (list new) (drop (inc idx) coll)))

(defn expt [base pow]
  (loop [exponent pow
         curr 1]
    (if (= exponent 0)
      curr
      (recur (dec exponent) (* base curr)))))

(defn padl [s n c]
  (str (str/join (repeat (- n (count s)) c))
       s))

(defn index-of [item coll]
  (defn index-of [e coll] (first (keep-indexed #(if (= e %2) %1) coll))))
