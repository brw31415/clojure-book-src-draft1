(ns hipstr.models.album-model
  (:require [yesql.core :refer [defqueries]]))

(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname     "//localhost/postgres"
              :user        "hipstr"
              :password    "p455w0rd"})

(defqueries "hipstr/models/albums.sql" {:connection db-spec})
