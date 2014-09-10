(ns hipstr.routes.home
  (:require [compojure.core :refer :all]
            [hipstr.layout :as layout]
            [hipstr.validators.user :as v]
            [hipstr.util :as util]
            [ring.util.response :as response]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defn signup-page-submit [user]
  (println user)
  (let [errors (v/validate-signup user)]
    (if (empty? errors)
      (response/redirect "/signup-success")
      (layout/render "signup.html" (assoc user :errors errors)))))

(defn signup-page []
  (layout/render "signup.html"))

(defroutes home-routes
  (GET   "/"        []       (home-page))
  (GET   "/about"   []       (about-page))
  (GET   "/signup"  []       (signup-page))
  (POST  "/signup"  [& form] (signup-page-submit form))
  (GET   "/signup-success" [] (str "success!")))
