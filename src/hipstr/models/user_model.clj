(ns hipstr.models.user-model
  (:require [crypto.password.bcrypt :as password]
            [hipstr.models.connection :refer [hipstr-db]]
            [noir.session :as session]
            [yesql.core :refer [defqueries]])
  (:use [korma.core]))

; herein lies thine DSL upon thee DSL, Korma
(declare users)

; declare our users table, which in our hipstr application
; is pretty straight forward.
; For Korma, however, we have to define the primary key because
; the name of the primary key is neither 'id' or 'users_id'
; ([tablename]_id)
(defentity users
  (pk :user_id))

; -- name: get-user-by-username
; -- Fetches a user from the DB based on username.
; SELECT *
; FROM users
; WHERE username=:username
(defn get-user-by-username
  "Gets a user by from the database by username."
  [username]
  (first (select users
          (where {:username username}))))

; -- name: insert-user<!
; -- Inserts a new user into the Users table
; -- Expects :username, :email, and :password
; INSERT INTO users (username, email, pass)
; VALUES (:username, :email, :password)
(defn insert-user<!
  "Inserts a user into our users table."
  [user]
  (insert users (values user)))

(defn add-user!
  "Saves a user to the database."
  [user]
  (let [new-user (clojure.set/rename-keys user {:password :pass})
        new-user (->> (password/encrypt (:pass new-user))
                     (assoc new-user :pass)
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
  (let [user (get-user-by-username username)]
    (if (and user (password/check password (:pass user)))
      (do
        (session/put! :user_id (:user_id user))
        (dissoc user :pass)))))

(defn is-authed?
  "Returns false if the current user is anonymous; otherwise true."
  [_]
  (not (nil? (session/get :user_id))))
