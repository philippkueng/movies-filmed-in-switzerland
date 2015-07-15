;; -*- coding: utf-8 -*-

(ns movies-in-switzerland.core
  (:require [clojure.string :as str]
            [instaparse.core :as insta]
            [clj-http.client :as client]
            [cheshire.core :refer :all]
            [cemerick.url :refer (url url-encode)]))

(defn write-to-file! [file-name data]
  (with-open [w (clojure.java.io/writer file-name :encoding "UTF-8")]
    (binding [*out* w]
      (clojure.pprint/write data))))

;; read the locations.list file and parse the lines
(defn raw-movie-lines
  []
  "Read the locations line by line as a sequence"
  (with-open [rdr (clojure.java.io/reader
                   "locations_list_switzerland_without_headers"
                   :encoding "ISO-8859-1")]
    (doall
     (line-seq rdr))))

(def movie-parser
  (insta/parser
   "line = name year series? type? location? meta-info?
    name = (<'\"'?>'('?#'(\\s?[\\p{L}0-9\\-\\/\\?\\!\\%\\&\\'\\,\\.:\\\\]+)+'')'?<'\"'?><#'\\s+'?>)*
    year = <'('>(#'[0-9]{4}'<#'[\\/\\p{L}]+'?>|#'\\?{4,5}')<')'><#'\\s+'?>
    series = <'{'><'{'?>#'[\\p{L}0-9-:()#\\.\\,\\'\\!\\?\\/ ]+'<'}'><'}'?><#'\\s+'?>
    type = <'('>('V'|'TV'|'VG')<')'><#'\\s+'?>
    location = #'[\\p{L}0-9,\\-\\.\\'\\! ]+'<#'\\s+'?>
    meta-info = ('('#'[\\p{L}0-9\\:\\'\\,\\-\\.\\[\\]\"\\/\\& ]+'')'#'\\s'?)*"
   :output-format :hiccup))

(defn fix-whitespace-with-openingbrackets
  [args]
  (->> args
       (map #(if (= % "(")
               " ("
               %))
       vec))

(defn format-movie
  [line]
  "Turns a line entry into a parsed map"
  (->> line
       movie-parser
       (insta/transform {:name (fn [& args]
                                 [:name (->> args
                                             fix-whitespace-with-openingbrackets
                                             (apply str))])
                         :meta-info (fn [& args]
                                      [:meta-info (apply str args)])})
       rest
       (into {})))


#_ (parse-all)

#_ (def line "Sauve qui peut (la vie) (1980)\t\t\t\tLausanne, Canton de Vaud, Switzerland")
#_ (def line "\"The Amazing Race\" (2001) {This Is More Important Than Your Pants Falling Down! (#3.8)}\tGrindelwald, Kanton Bern, Switzerland\t(field) (roadblock)")
#_ (def line "\"Anthony Bourdain: No Reservations\" (2005) {Vietnam: The Island of Mr. Sang (#1.4)}\tSwiss Alps, Switzerland\t(archive footage)")
#_ (def line "Bachna Ae Haseeno (2008)\t\t\t\tSwitzerland\t(\"Mahi - 1996\" storyline)")
#_ (def line "\"Anthony Bourdain: No Reservations\" (2005) {Vietnam: The Island of Mr. Sang (#1.4)}\tSwiss Alps, Switzerland\t(archive footage) (foobar)")
#_ (def line "\"Anthony Bourdain: No Reservations\" (2005) {Vietnam: The Island of Mr. Sang (#1.4)}\tSwiss Alps, Switzerland\t(archive footage)")
#_ (def line "\"Best Friends\" (2010)\t\t\t\t\tWettingen, Kanton Aargau, Switzerland\t(exteriors and interiors Switzerland)")
#_ (def line "South of Hope Street (2009) {{SUSPENDED}}\t\tSwiss Alps, Switzerland")
#_ (def line "Absinthe (2010/I)\t\t\t\t\tSwitzerland (foobar) (asdf)")
#_ (def line "\"The Amazing Race\" (2001) {This Is More Important Than Your Pants Falling Down! (#3.8)}\tChalet Arnika, Grindelwald, Kanton Bern, Switzerland\t(pit stop)")
#_ (def line "\"Coach Trip\" (2005) {Geneva, Switzerland (#6.5)}\tGeneva, Canton de Geneve, Switzerland")
#_ (def line "\"Das Experiment - Wo ist dein Limit?\" (2013)\t\tSwitzerland")
#_ (def line "\"Coach Trip\" (2005) {Zermatt, Switzerland (#6.3)}\tZermatt, Kanton Wallis, Switzerland")
#_ (def line "\"Bilderbuch Deutschland\" (1996) {Am Schweizer Bodensee}\tSt. Gallen, Kanton St. Gallen, Switzerland")
#_ (def line "\"Anthony Bourdain: No Reservations\" (2005) {Vietnam: The Island of Mr. Sang (#1.4)}\tSwiss Alps, Switzerland\t(archive footage)")
#_ (def line "\"8x45 - Austria Mystery\" (2006) {Bis in den Tod (#1.6)}\tSwitzerland")
#_ (def line2 "Der Film Name (2015)")
#_ (def line "Yaraana (1995)\t\t\t\t\t\tGstaad, Kanton Bern, Switzerland")
#_(def line "\"2008 UEFA European Football Championship\" (2008)		St Jakob Stadium, Basel, Kanton Basel Stadt, Switzerland")
#_ (def line "\"Bilderbuch Deutschland\" (1996) {Am Schweizer Bodensee}	Stein am Rhein, Kanton Schaffhausen, Switzerland")
#_ (def line "\"77 Sunset Strip\" (1958) {Our Man in Switzerland (#5.33)}	Stage 21, Warner Brothers Burbank Studios - 4000 Warner Boulevard, Burbank, California, USA")
#_ (def line "\"77 Sunset Strip\" (1958) {Our Man in Switzerland (#5.33)}")
#_ (def line "Yes: Live at Montreux 2003 (2007) (V)			Montreux, Canton de Vaud, Switzerland")
#_ (movie-parser line)
#_ (into {} (rest (movie-parser line)))
#_ (format-movie line)
#_(def line "\"2008 UEFA European Football Championship\" (2008)")
#_ (def line "Yes: Live at Montreux 2003 (2007) (V)			Montreux, Canton de Vaud, Switzerland	(Montreux Jazz Festival)")

#_(format-movie line)
#_(pr-str (map format-movie raw-movie-lines))
#_(pr-str (map split-movie-location-line raw-movie-lines))

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


(defn enrich
  []
  "Enrich the data with latitude and longitude"
  (->> (raw-movie-lines)
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

#_ (defn parse-all
     []
     (->> (raw-movie-lines)
          (map format-movie)
          (write-to-file! "movie-parsed.edn")))

#_ (enrich)

;; read the edn file, strip out the unnecessary keys and save as json
(defn convert-edn-to-json
  []
  (->> (-> "movie-year-location.edn"
           slurp
           read-string
           (generate-string {:escape-non-ascii true}))
       (spit "movie-year-location.json")))

;; url encode the location data
#_(fetch-geo-data "Siebnen, Switzerland")

#_(split-movie-location-line (first raw-movie-lines))
#_(str/split (first raw-movie-lines) #"\t")
#_ (first raw-movie-lines)

