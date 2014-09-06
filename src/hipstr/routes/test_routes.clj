(ns hipstr.routes.test-routes
  (:require [compojure.core :refer :all]))

(defn render-request-val [m & [k]]
  "Simply returns the value of key k in map m, if k is provided;
   Otherwise return the original m.
   If k is provided, but not found in m, a message indicating as such
   will be returned. In real life you'll probably want to just return nil,
   or raise an exception... doing what I'm doing here will get you raged on."
  (let [result (if k
                (or ((keyword k) m) (str k "is not a valid key."))
                m)]
    (str result)))

(defroutes test-routes
  (GET "/req" request (render-request-val request))
  ;(GET "/req/:val" [val] (str val)) ;no access to the full request map
  (GET "/req/:val" [val :as full-req] (str val "<br>" full-req)) ; use :as to get access to full request map
  (GET "/req/key/:key" [key :as request] (render-request-val request key))
  (GET "/req/:val/:another-val/:and-another" [val & remainders] (str val "<br>" remainders))) ;use & to get access to unbound params
