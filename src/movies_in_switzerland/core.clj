(ns movies-in-switzerland.core
  (:require [clojure.string :as str]
            [clj-http.client :as client]
            [cheshire.core :refer :all]))

;; read the locations.list file and count the lines
(def raw-movie-lines
  (with-open [rdr (clojure.java.io/reader
                   "locations_list_switzerland_without_headers")]
    (doall
     (take 5 (line-seq rdr)))))

(defn split-movie-location-line
  [line]
  "Splits a line entry with a movie, year, optional series info and location"
  (remove #(= "" %)
          (str/split line #"[\)]?\t|\"[\s]?[\(]?|\)\s\{|\}")))

(defn fetch-geo-data
  [location-str]
  "Fetch the location information from Yahoo"
  (:Result (:results (:query (parse-string (:body (client/get "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text%3D%22sfo%22&format=json&callback=")) true)))))


;; url encode the location data
#_(fetch-geo-data "Siebnen, Switzerland")

#_(split-movie-location-line (first raw-movie-lines))
#_(str/split (first raw-movie-lines) #"\t")
#_ (first raw-movie-lines)

(pr-str (map split-movie-location-line raw-movie-lines))

