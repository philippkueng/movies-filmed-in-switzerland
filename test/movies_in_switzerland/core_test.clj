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

(deftest line-sample-6
  (testing "That the line is split correctly"
    (is (= {:name "8x45 - Austria Mystery"
            :year "2006"
            :series "Bis in den Tod (#1.6)"
            :location "Switzerland"}
           (format-movie "\"8x45 - Austria Mystery\" (2006) {Bis in den Tod (#1.6)}\tSwitzerland")))))

(deftest line-sample-7
  (testing "That the line is split correctly"
    (is (= {:name "Anthony Bourdain: No Reservations"
            :year "2005"
            :series "Vietnam: The Island of Mr. Sang (#1.4)"
            :location "Swiss Alps, Switzerland"
            :meta-info "(archive footage)"}
           (format-movie "\"Anthony Bourdain: No Reservations\" (2005) {Vietnam: The Island of Mr. Sang (#1.4)}\tSwiss Alps, Switzerland\t(archive footage)")))))

(deftest line-sample-8
  (testing "That the line is split correctly"
    (is (= {:name "Bilderbuch Deutschland"
            :year "1996"
            :series "Am Schweizer Bodensee"
            :location "St. Gallen, Kanton St. Gallen, Switzerland"}
           (format-movie "\"Bilderbuch Deutschland\" (1996) {Am Schweizer Bodensee}\tSt. Gallen, Kanton St. Gallen, Switzerland")))))

(deftest line-sample-9
  (testing "That the line is split correctly"
    (is (= {:name "Coach Trip"
            :year "2005"
            :series "Zermatt, Switzerland (#6.3)"
            :location "Zermatt, Kanton Wallis, Switzerland"}
           (format-movie "\"Coach Trip\" (2005) {Zermatt, Switzerland (#6.3)}\tZermatt, Kanton Wallis, Switzerland")))))

(deftest line-sample-10
  (testing "That the line is split correctly"
    (is (= {:name "Das Experiment - Wo ist dein Limit?"
            :year "2013"
            :location "Switzerland"}
           (format-movie "\"Das Experiment - Wo ist dein Limit?\" (2013)\t\tSwitzerland")))))

(deftest line-sample-11
  (testing "That the line is split correctly"
    (is (= {:name "Absinthe"
            :year "2010"
            :location "Switzerland"}
           (format-movie "Absinthe (2010/I)\t\t\t\t\tSwitzerland")))))

(deftest line-sample-12
  (testing "That the line is split correctly"
    (is (= {:name "Bachna Ae Haseeno"
            :year "2008"
            :location "Switzerland"
            :meta-info "(\"Mahi - 1996\" storyline)"}
           (format-movie "Bachna Ae Haseeno (2008)\t\t\t\tSwitzerland\t(\"Mahi - 1996\" storyline)")))))
