(ns hipstr.routes.home
  (:require [compojure.core :refer :all]
            [hipstr.layout :as layout]
            [hipstr.signup :as signup]
            [hipstr.util :as util]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defn signup-page-submit [user]
  (println user)
  (let [errors (signup/validate-signup user)]
    (if (empty? errors)
      (layout/render "signup-success.html")
      (layout/render "signup.html" (assoc user :errors errors)))))

(defn signup-page []
  (layout/render "signup.html"))

(defroutes home-routes
  (GET   "/"        []       (home-page))
  (GET   "/about"   []       (about-page))
  (GET   "/signup"  []       (signup-page))
  (POST  "/signup"  [& form] (signup-page-submit form)))
