(ns hipstr.test.validators.user-test
  (:use  clojure.test
         hipstr.validators.user))

(deftest email-validator-returns-empty-when-email-is-valid
  (is (empty? (email-validator {:email "dude@bides.net"}))
      "email-validator failed to validate valid email."))

(deftest email-validator-returns-1-error-when-blank
  (let [results (:email (email-validator {}))]
    (is (= 1 (count results)))
    (is (= "is a required field" (first results)))))

(deftest email-validator-returns-1-error-when-invalid
  (let [results (:email (email-validator {:email "dude@bides"}))]
   (is (= 0 (count results)))
   (is (= "The email's format is incorrect" (first results)))))
