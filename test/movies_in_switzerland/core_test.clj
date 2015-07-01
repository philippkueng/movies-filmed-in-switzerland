(ns movies-in-switzerland.core-test
  (:require [clojure.test :refer :all]
            [movies-in-switzerland.core :refer :all]))

(deftest line-sample-1
  (testing "That the line is split correctly"
    (is (= {:name "2008 UEFA European Football Championship"
            :year "2008"
            :location "St Jakob Stadium, Basel, Kanton Basel Stadt, Switzerland"}
           (format-movie "\"2008 UEFA European Football Championship\" (2008)		St Jakob Stadium, Basel, Kanton Basel Stadt, Switzerland")))))

#_(deftest line-sample-2
  (testing "That the line is split correctly"
    (is (= {:name "Yaraana"
            :year "1995"
            :location "Gstaad, Kanton Bern, Switzerland"}
           (format-movie "Yaraana (1995)						Gstaad, Kanton Bern, Switzerland")))))

