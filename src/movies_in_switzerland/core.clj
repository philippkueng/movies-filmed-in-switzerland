(ns movies-in-switzerland.core
  (:require [clojure.string :as str]
            [clj-http.client :as client]
            [cheshire.core :refer :all]
            [cemerick.url :refer (url url-encode)]))

;; read the locations.list file and count the lines
(defn split-movie-location-line
  [line]
  "Splits a line entry with a movie, year, optional series info and location"
  (remove #(= "" %)
          (str/split line #"[\)]?\t|\"[\s]?[\(]?|\)\s\{|\}")))

(def raw-movie-lines
  (with-open [rdr (clojure.java.io/reader
                   "locations_list_switzerland_without_headers")]
    (doall
     (take 5 (line-seq rdr)))))


;; make a location request against yahoo's yql service
(defn generate-url
  [location]
  "Given the location data generates the url to call"
  (str "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text%3D%22"
       (url-encode location)
       "%22&format=json&callback="))

(defn fetch-geo-data
  [location]
  "Fetch the location information from Yahoo"
  (:Result (:results (:query
                      (parse-string
                       (:body (client/get (generate-url location)))
                       true)))))


;; url encode the location data
#_(fetch-geo-data "Siebnen, Switzerland")

#_(split-movie-location-line (first raw-movie-lines))
#_(str/split (first raw-movie-lines) #"\t")
#_ (first raw-movie-lines)

(pr-str (map split-movie-location-line raw-movie-lines))

