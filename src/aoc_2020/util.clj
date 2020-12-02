(ns aoc-2020.util
  (:require
    [clojure.java.io :as io]
    [clojure.string :as str]))

(defn read-file [filename]
  (->> (io/file filename)
       slurp
       str/split-lines
       (map str/trim)))
