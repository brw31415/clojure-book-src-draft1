(ns hipstr.routes.albums
  (:require [compojure.core :refer :all]
            [hipstr.layout :as layout]
            [hipstr.validators.album :as v]
            [hipstr.models.album-model :as album]
            [taoensso.timbre :as timbre]))

(defn render-recently-added-page
  "Simply renders the recently added page with the given context."
  [ctx]
  (layout/render "albums/recently-added.html" ctx))

(defn recently-added-page
  "Renders out the recently-added page."
  []
  (render-recently-added-page {:albums (album/get-recently-added)}))

(defn recently-added-submit
  "Handles the add-album form on the recently-added page."
  [album]
  (let [errors (v/validate-new-album album)
        ctx (if (not-empty errors)
              {:validation-errors errors :new album}
              (try
                (album/add-album! album)
                {:new {} :success true}
                (catch Exception e
                  (timbre/error e)
                  {:new album :error "Oh snap! We lost the album. Try it again?"})))
        ctx (assoc ctx :albums (album/get-recently-added))]
    (render-recently-added-page ctx)))

(defn albums-by-artist-page
  "Renders a page displaying the discography for a given artist."
  [artist]
  (layout/render "albums/by-artist.html" {:artist artist
                                          :albums (album/get-by-artist {:artist artist})}))

(defroutes album-routes
  (GET "/albums/recently-added" [] (recently-added-page))
  (POST "/albums/recently-added" [& album-form] (recently-added-submit album-form))
  (GET "/albums/:artist" [artist] (albums-by-artist-page artist)))
