(ns hipstr.routes.home
  (:require [compojure.core :refer :all]
            [hipstr.cookies :as cookies]
            [hipstr.layout :as layout]
            [hipstr.models.user-model :as u]
            [hipstr.util :as util]
            [hipstr.validators.user :as v]
            [noir.session :as session]
            [ring.util.response :as response]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defn login-page
  ([]
   (layout/render "login.html" {:username (cookies/remember-me)}))
  ([credentials]
   (println credentials)
   (if (apply u/auth-user (map credentials [:username :password]))
     (do (if (:remember-me credentials)
           (cookies/remember-me (:username credentials))
           (cookies/remember-me nil))
       (response/redirect "/albums/recently-added"))
     (layout/render "login.html" {:invalid-credentials? true}))))

(defn logout []
  "Logs the user out of this session."
  (u/invalidate-auth)
  (response/redirect "/"))

(defn signup-page
  ([]
   (layout/render "signup.html"))
  ([user]
  (let [errors (v/validate-signup user)]
    (if (not (empty? errors))
      (layout/render "signup.html" (assoc user :errors errors))
      (do
        (u/add-user! user)
        (response/redirect "/login"))))))

(defroutes home-routes
  (GET   "/"        []       (home-page))
  (GET   "/about"   []       (about-page))
  (GET   "/login"   []       (login-page))
  (POST  "/login"   [& form] (login-page form))
  (ANY   "/logout"  []       (logout))
  (GET   "/signup"  []       (signup-page))
  (POST  "/signup"  [& form] (signup-page form)))
