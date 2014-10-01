(ns hipstr.handler
  (:require [compojure.core :refer [defroutes]]
            [hipstr.routes.albums :refer [album-routes]]
            [hipstr.routes.home :refer [home-routes]]
            [hipstr.routes.test-routes :refer [test-routes]]
            [hipstr.middleware :refer [load-middleware]]
            [hipstr.session-manager :as session-manager]
            [noir.response :refer [redirect]]
            [noir.util.middleware :refer [app-handler]]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [taoensso.timbre.appenders.rotor :as rotor]
            [taoensso.timbre.appenders.rolling :as rolling]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [cronj.core :as cronj]))

(defroutes base-routes
  (route/resources "/" {:root "public"})
  (route/not-found "Not Found")
  (route/files "/downloads" {:root "downloads"}))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config! [:appenders :standard-out :min-level] :info)

  (timbre/set-config!
   [:appenders :rolling]
   (rolling/make-rolling-appender {:min-level :info}))

  (timbre/set-config!
   [:shared-appender-config :rolling :path] "logs/hipstr.log")

  (if (env :dev) (parser/cache-off!))
  ;;start the expired session cleanup job
  (cronj/start! session-manager/cleanup-job)
  (timbre/info "hipstr started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "hipstr is shutting down...")
  (cronj/shutdown! session-manager/cleanup-job)
  (timbre/info "shutdown complete!"))

(def app (app-handler
           ;; add your application routes here
           [album-routes home-routes base-routes test-routes]
           ;; add custom middleware here
           :middleware (load-middleware)
           ;; timeout sessions after 30 minutes
           :session-options {:timeout (* 60 30)
                             :timeout-response (redirect "/")}
           ;; add access rules here
           :access-rules []
           ;; serialize/deserialize the following data formats
           ;; available formats:
           ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
           :formats [:json-kw :edn]))
