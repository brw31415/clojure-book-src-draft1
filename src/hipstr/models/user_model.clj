(ns hipstr.models.user-model
  (:require [crypto.password.bcrypt :as password]
            [hipstr.models.connection :refer [db-spec]]
            [noir.session :as session]
            [yesql.core :refer [defqueries]]))

(defqueries "hipstr/models/users.sql" {:connection db-spec})

(defn add-user!
  "Saves a user to the database."
  [user]
  (let [new-user (->> (password/encrypt (:password user))
                     (assoc user :password)
                     insert-user<!)]
    (dissoc new-user :pass)))

(defn invalidate-auth
  "Invalidates a user's current authenticated state."
  []
  (session/clear!))

(defn auth-user
  "Validates a username/password and, if they match, adds the user_id to the session and returns the user map from
  the database. Otherwise nil."
  [username password]
  (println "username " username " password " password)
  (let [user (first (get-user-by-username {:username username}))]
    (if (and user (password/check password (:pass user)))
      (do
        (session/put! :user_id (:user_id user))
        (dissoc user :pass)))))

(defn is-authed?
  "Returns false if the current user is anonymous; otherwise true."
  [_]
  (not (nil? (session/get :user_id))))
