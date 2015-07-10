(ns movies-in-switzerland.core
  (:require [clojure.string :as str]
            [instaparse.core :as insta]
            [clj-http.client :as client]
            [cheshire.core :refer :all]
            [cemerick.url :refer (url url-encode)]))

#_(defn write-to-file! [file-name data]
  (with-open [w (clojure.java.io/writer file-name)]
    (binding [*out* w]
      (clojure.pprint/write data))))

;; read the locations.list file and parse the lines
#_(def raw-movie-lines
  (with-open [rdr (clojure.java.io/reader
                   "locations_list_switzerland_without_headers")]
    (doall
     (take 5 (line-seq rdr)))))

#_(defn split-movie-location-line
  [line]
  "Splits a line entry with a movie, year, optional series info and location"
  (next
   (re-matches
    #"[\"]?([\d\w ]+?)[\"]?[ ]?\((\d{4})\)([\{[\w \d]+\(\#[\d\.]+\)\}]?)[\t]+(.*)"
    line)))

(def movie-parser
  (insta/parser
   "line = name year series? type? location? meta-info?
    name = <'\"'?>#'(\\s?[\\p{L}0-9\\-\\?\\!\\%\\&\\'\\,\\.:]+)+'<'\"'?><#'\\s+'?>
    year = <'('>(#'[0-9]{4}'<#'[\\/\\p{L}]+'?>|#'\\?{4,5}')<')'><#'\\s+'?>
    series = <'{'><'{'?>#'[\\p{L}0-9-:()#\\.\\,\\'\\!\\?\\/ ]+'<'}'><'}'?><#'\\s+'?>
    type = <'('>('V'|'TV')<')'><#'\\s+'?>
    location = #'[\\p{L}0-9,\\-\\.\\'\\! ]+'<#'\\s+'?>
    meta-info = ('('#'[\\p{L}0-9\\:\\'\\,\\-\\.\\[\\]\" ]+'')'#'\\s'?)*"
   :output-format :hiccup))

(defn format-movie
  [line]
  "Turns a line entry into a parsed map"
  (->> line
       movie-parser
       (insta/transform {:meta-info (fn [& args]
                                      [:meta-info (apply str args)])})
       rest
       (into {})))



;; make a location request against yahoo's yql service
(defn generate-url
  [location]
  "Given the location data generates the url to call"
  (str "https://query.yahooapis.com/v1/public/yql?q="
       "select%20*%20from%20geo.placefinder%20where%20text%3D%22"
       (url-encode location)
       "%22&format=json&callback="))

(defn ask-yql
  [url]
  (try
    (client/get url)
    (catch Exception e (ask-yql url))))

(defn fetch-geo-data
  [location]
  "Fetch the location information from Yahoo"
  (-> location
      generate-url
      ask-yql
      :body
      (parse-string true)
      :query
      :results
      :Result))


#_(defn enrich
  []
  "Enrich the data with latitude and longitude"
  (->> raw-movie-lines
       (map format-movie)
       (map (fn [movie]
              (merge movie
                     (-> movie
                         :location
                         fetch-geo-data
                         (select-keys [:statecode :city :uzip :county
                                       :longitude :state :latitude :country
                                       :countrycode])))))
       (write-to-file! "movie-year-location.edn")))

;; url encode the location data
#_(fetch-geo-data "Siebnen, Switzerland")

#_(split-movie-location-line (first raw-movie-lines))
#_(str/split (first raw-movie-lines) #"\t")
#_ (first raw-movie-lines)

