(ns hipstr.models.album-model
  (:require [yesql.core :refer [defqueries]]
            [clojure.java.jdbc :as jdbc]
            [taoensso.timbre :as timbre]
            [hipstr.models.connection :refer [db-spec]]))

(defqueries "hipstr/models/albums.sql" {:connection db-spec})
(defqueries "hipstr/models/artists.sql" {:connection db-spec})

(defn add-album!
  "Adds a new album to the database."
  ([album]
     (jdbc/with-db-transaction [tx db-spec]
       (add-album! album tx)))
  ([album tx]
   (let [artist-info {:artist_name (:artist_name album)}
         ; fetch or insert the artist record
         artist (or (first (get-artist-by-name artist-info {:connection tx}))
                    (insert-artist<! artist-info {:connection tx}))
         album-info (assoc album :artist_id (:artist_id artist))]
     (timbre/info (str "artist: " artist))
     (timbre/info (str "album-info: " album-info))
     (or (first (get-album-by-name album-info {:connection tx}))
         (insert-album<! album-info {:connextion tx})))))
