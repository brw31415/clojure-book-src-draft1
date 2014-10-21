(ns hipstr.validators.album
  (:require [validateur.validation :refer :all]
            [noir.validation :as v]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def release-date-formatter
  (f/formatter "yyyy-mm-dd"))

(def release-date-format-message
  "The release date's format is incorrect. Must be yyyy-mm-dd.")

(def release-date-invalid-message
  "The release date is not a valid date.")

(defn parse-date
  "Returns a date/time object if the provided date-string is valid; otherwise nil."
  [date]
  (try
    (f/parse release-date-formatter date)
    (catch Exception e)))

(def release-date-format-validator
  "Returns a validator function which ensures the format of the date-string is correct."
  (format-of :release_date
                :format #"^\d{4}-\d{2}-\d{2}$"
                :blank-message release-date-format-message
                :message release-date-format-message))

(def release-date-validations
  "Returns a validator which, when the format of the date-string is correct,
   ensures the date itself is valid."
  (validation-set
   release-date-format-validator
   (validate-when #(valid? (validation-set release-date-format-validator) %)
                  (validate-with-predicate :release_date
                                           #(v/not-nil? (parse-date (:release_date %)))
                                           :message release-date-invalid-message))))

(def album-validations
  "Returns a validation set, ensuring an artist_name and album_name are present."
  (validation-set
   (presence-of :artist_name
                :message "Artist is required.")
   (presence-of :album_name
                :message "Album is required.")))

(def validate-new-album
  "Retruns a function which runs an input map through the gauntlet of validation-sets,
   ensuring our artist and album meet the requirements."
  (compose-sets release-date-validations album-validations))
