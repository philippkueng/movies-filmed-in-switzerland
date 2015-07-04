(ns movies-in-switzerland.core-test
  (:require [clojure.test :refer :all]
            [movies-in-switzerland.core :refer :all]))

(deftest line-sample-1
  (testing "That the line is split correctly"
    (is (= {:name "2008 UEFA European Football Championship"
            :year "2008"
            :location "St Jakob Stadium, Basel, Kanton Basel Stadt, Switzerland"}
           (format-movie "\"2008 UEFA European Football Championship\" (2008)		St Jakob Stadium, Basel, Kanton Basel Stadt, Switzerland")))))

(deftest line-sample-2
  (testing "That the line is split correctly"
    (is (= {:name "Yaraana"
            :year "1995"
            :location "Gstaad, Kanton Bern, Switzerland"}
           (format-movie "Yaraana (1995)						Gstaad, Kanton Bern, Switzerland")))))

(deftest line-sample-4
  (testing "That the line is split correctly"
    (is (= {:name "Yes: Live at Montreux 2003"
            :year "2007"
            :type "V"
            :location "Montreux, Canton de Vaud, Switzerland"}
           (format-movie "Yes: Live at Montreux 2003 (2007) (V)			Montreux, Canton de Vaud, Switzerland")))))

(deftest line-sample-5
  (testing "That the line is split correctly"
    (is (= {:name "A Girl Named Sooner"
            :year "1975"
            :type "TV"
            :location "Switzerland County, Indiana, USA"}
           (format-movie "A Girl Named Sooner (1975) (TV)				Switzerland County, Indiana, USA")))))

