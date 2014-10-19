(ns hipstr.models.album-model
  (:require [yesql.core :refer [defqueries]]
            [taoensso.timbre :as timbre]
            [hipstr.models.connection :refer [db-spec]]))

(defqueries "hipstr/models/albums.sql" {:connection db-spec})
(defqueries "hipstr/models/artists.sql" {:connection db-spec})

(defn add-album!
  "Adds a new album to the database."
  ([album]
   (add-album! album db-spec))

  ([album tx]
   (let [artist-info {:artist_name (:artist_name album)}

         ; fetch or insert the artist record
         artist (or (first (get-artist-by-name artist-info {:connection tx}))
                    (insert-artist<! artist-info {:connection tx}))

         album-info (assoc album :artist_id (:artist_id artist))]

     (or (first (get-album-by-name album-info {:connection tx}))
         (insert-album<! album-info {:connextion tx})))))
