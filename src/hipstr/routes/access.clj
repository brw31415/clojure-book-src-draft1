(ns hipstr.routes.access
  (:require [hipstr.models.user-model :refer [is-authed?]]
            [ring.util.response :as response]))

   ;{:redirect "/login" :rule is-authed?} ;default access rule.

(def rules
  "The rules for accessing various routes in our application."
  [{:uris ["/just-for-you" "/and-maybe-you"]
    :rule #(-> (java.util.UUID/randomUUID) str keyword %)
    :on-fail (fn [req] (response/status (response/response "Unauthorized") 403))}])
