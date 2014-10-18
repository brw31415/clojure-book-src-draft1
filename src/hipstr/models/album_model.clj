(ns hipstr.models.album-model
  (:require [yesql.core :refer [defqueries]]
            [taoensso.timbre :as timbre]
            [hipstr.models.connection :refer [db-spec]]))

(defqueries "hipstr/models/albums.sql" {:connection db-spec})
(defqueries "hipstr/models/artists.sql" {:connection db-spec})

(defn add-album!
  "Adds a new album to the database."
  [album]
  (timbre/info album)
  (let [artist-info {:artist_name (:artist_name album)}
        artist (or (get-artist-by-name artist-info)
                   (insert-artist<! artist-info))
        album-info (assoc album :artist_id (:artist_id artist))]
    (timbre/info artist)
    (timbre/info album-info)
    (or (get-album-by-name album-info)
        (insert-album<! album-info))))
