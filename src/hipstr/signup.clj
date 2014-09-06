(ns hipstr.signup
  (:require [validateur.validation :refer :all]
            [noir.validation :as v]))

(def email-validator
  (validation-set
   (presence-of :email)
   (validate-with-predicate :email
                            #(v/is-email? (:email %)))))

(def username-validator
  (validation-set
   (presence-of :username)
   (format-of :username
              :format #"^[a-zA-Z0-9_]*$"
              :message "Only letters, numbers, and underscores allowed.")))

(def password-validator
  (validation-set
   (presence-of :password)
   (length-of :password
              :within (range 8 101)
              :message-fn (fn [type m attribute & args]
                            "Passwords must be between 8 and 100 characters long."))))

(defn validate-signup [signup]
  "Validates the incoming signup map and returns a set of error messages
   for any invalid field."
  (let [v (validation-set
            (presence-of #{:username :email :password})
            (format-of :email
              :format #"\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b"
              :message "The format of the email address appears to be wrong.")
            (format-of :username
                       :format #"^[a-zA-Z0-9_]*$"
                       :message "Only letters, numbers, and underscores allowed.")
            (length-of :password
                      :within (range 8 101)
                      :message-fn (fn [type m attribute & args]
                                    "Passwords must be between 8 and 100 characters long.")))]
    (v signup)))

(defn validate-signup2 [signup]
  ((compose-sets email-validator username-validator password-validator) signup))
