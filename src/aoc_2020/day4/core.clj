(ns aoc-2020.day4.core
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [aoc-2020.util :refer [read-file]]))

;byr (Birth Year)
;iyr (Issue Year)
;eyr (Expiration Year)
;hgt (Height)
;hcl (Hair Color)
;ecl (Eye Color)
;pid (Passport ID)
;cid (Country ID)

(defn make-document [raw-data]
  (->> (str/split raw-data #"[\s:]+")
       (apply hash-map)))

(defn read-passports []
  (->> (read-file "./src/aoc_2020/day4/input1.txt")
       (#(str/split % #"\n\n"))
       (map make-document)))

(defn valid-passport-format? [document]
  (let [required-fields (list "byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid")]
    (every? document required-fields)))

(def passport-rules
  {"byr" #(let [entry (edn/read-string %)]
            (and (>= entry 1920) (<= entry 2002)))
   "iyr" #(let [entry (edn/read-string %)]
            (and (>= entry 2010) (<= entry 2020)))
   "eyr" #(let [entry (edn/read-string %)]
            (and (>= entry 2020) (<= entry 2030)))
   "hgt" #(if-let [hgt-match (re-matches #"(\d+)(in|cm)" %)]
            (let [unit (nth hgt-match 2)
                  height (edn/read-string (second hgt-match))
                  limits {"in" (fn [h] (and (>= h 59) (<= h 76)))
                          "cm" (fn [h] (and (>= h 150) (<= h  193)))}
                  valid? (limits unit)]
              (valid? height))
            nil)
   "hcl" #(re-matches #"#[0-9a-f]{6}" %)
   "ecl" #(contains? #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} %)
   "pid" #(re-matches #"\d{9}" %)
   })

(defn validated-passport? [document]
  (let [validate-fields (keys passport-rules)]
    (every? (fn [field] ((passport-rules field) (document field)))
            validate-fields)))

(defn part1 []
  (->> (read-passports)
       (filter #(valid-passport-format? %))
       (count)))

(defn part2 []
  (->> (read-passports)
       (filter #(and (valid-passport-format? %)
                     (validated-passport? %)))
       (count)))
