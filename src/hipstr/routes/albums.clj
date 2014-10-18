(ns hipstr.routes.albums
  (:require [compojure.core :refer :all]
            [hipstr.layout :as layout]
            [hipstr.validators.album :as v]
            [hipstr.models.album-model :as album]
            [taoensso.timbre :as timbre]))

(defn recently-added-page
  "Renders out the recently-added page."
  []
  (layout/render "albums/recently-added.html" {:albums (album/get-recently-added)}))

(defn recently-added-submit
  "Handles the add-album form on the recently-added page."
  [album]
  (let [errors (v/validate-new-album album)
        albums (album/get-recently-added)
        ctx {:albums albums :errors errors :new (if (empty? errors) {} album) :success true}]
    (if (not (empty? errors))
      (layout/render "albums/recently-added.html" ctx)
      (try
        (album/add-album! album)
        (layout/render "albums/recently-added.html" ctx)
        (catch Exception e
          (timbre/error e)
          (layout/render "albums/recently-added.html" (merge ctx {:success false :error (.getMessage e)})))))))

(defn albums-by-artist-page
  "Renders a page displaying the discography for a given artist."
  [artist]
  (layout/render "albums/by-artist.html" {:artist artist
                                          :albums (album/get-by-artist {:artist artist})}))

(defroutes album-routes
  (GET "/albums/recently-added" [] (recently-added-page))
  (POST "/albums/recently-added" [& album-form] (recently-added-submit album-form))
  (GET "/albums/:artist" [artist] (albums-by-artist-page artist)))
