(ns hipstr.routes.test-routes
  (:require [compojure.core :refer :all]))

(defn render-request-val [m & [k]]
  "Simply returns the value of key k in map m, if k is provided;
   Otherwise return the original m.
   If k is provided, but not found in m, a message indicating as such
   will be returned. In real life you'll probably want to just return nil,
   or raise an exception... doing what I'm doing here will get you raged on."
  (str (if k
         (if-let [result ((keyword k) m)]
           result
           (str k " is not a valid key."))
         m)))

(defroutes test-routes
  (GET "/req" request (render-request-val request))

  ;no access to the full request map
  ;(GET "/req/:val" [val] (str val))

  ; use :as to get access to full request map
  (GET "/req/:val" [val :as full-req] (str val "<br>" full-req))

  ;use & to get access to unbound params
  (GET "/req/:val/:another-val/:and-another" [val & remainders] (str val "<br>" remainders))

    ; order is important. /req/:key will never match because
  ; /req/:val precedes it. Routes are matched on URL structure.
  (GET "/req/key/:key" [key :as request] (render-request-val request key)))
