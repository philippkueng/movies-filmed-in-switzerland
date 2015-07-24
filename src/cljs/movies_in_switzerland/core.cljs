(ns ^:figwheel-always movies-in-switzerland.core
    (:require [reagent.core :as reagent :refer [atom]]
              [ajax.core :refer [GET]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world to you!"}))
(defonce movies (atom nil))

(defn load-movies
  "Fetches the movies data and parses it then assignes it to the movies atom"
  []
  (GET "/movie-year-location.json"
       {:response-format :json
        :keywords? true
        :handler (fn [response]
                   (reset! movies response))}))

(defn hello-world []
  [:div.ui.active
   (if (nil? @movies)
     (list      
      [:h1.ui.header.centered (:text @app-state)]
      [:br]
      [:div.ui.active.inline.medium.text.loader.centered "Loading assets..."])
     (list
      [:p (str "Loaded "
               (count @movies)
               " movies.")]
      [:ul
       (for [movie @movies]
         [:li (:name movie)])]))])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
