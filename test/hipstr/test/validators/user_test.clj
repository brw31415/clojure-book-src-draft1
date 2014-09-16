(ns hipstr.test.validators.user-test
  (:use clojure.test
        hipstr.validators.user))

(defn validate-email [email]
  "Validates the provided email for us, and returns the
   set of validation messages for the email, if any."
  (:email (email-validator {:email email})))

(defn validate-username [username]
  "Validates the provided username for us, and returns the
   set of validation messages for the username, if any."
  (:username (username-validator {:username username})))

(defn validate-password [password]
  "Validates the provided password for us, and returns the
   set of validation messages for the username, if any."
  (:password (password-validator {:password password})))

(deftest blank-email-returns-email-is-required-message
  (let [result (validate-email "")]
    (is (= 1 (count result)))
    (is (= email-blank-msg (first result)))))

(deftest invalid-email-returns-appropriate-message
  (let [result (validate-email "dude@bides.")]
    (is (= 1 (count result)))
    (is (= email-format-msg, (first result)))))

(deftest valid-email-returns-0-messages
  (let [result (validate-email "dude@bides.net")]
    (is (= 0 (count result)))))

(deftest blank-username-returns-a-username-required-message
  (let [result (validate-username "")]
    (is (= 1 (count result)))
    (is (= username-blank-msg (first result)))))

(deftest invalid-username-returns-appropriate-message
  (let [result (validate-username "Yea! Spaces! Illegal Characters!")]
    (is (= 1 (count result)))
    (is (= username-invalid-msg (first result)))))

(deftest valid-username-returns-0-messages
  (let [result (validate-username "TheDude")]
    (is (= 0 (count result)))))

(deftest password-must-be-at-least-8-characters-long
  (let [result (validate-password "123456")]
    (is (= 1 (count result)))
    (is (= password-invalid-msg (first result)))))

(deftest password-must-be-less-than-100-characters-long
  (let [long-password (clojure.string/join (repeat 101 "a"))
        result (validate-password long-password)]
    (is (= 1 (count result)))
    (is (= password-invalid-msg (first result)))))

(deftest blank-password-returns-a-password-required-message
  (let [result (validate-password "")]
    (is (= 1 (count result)))
    (is (= password-blank-msg (first result)))))

(deftest valid-albeit-crappy-password-returns-0-messages
  (let [result (validate-password "12345678")]
    (is (= 0 (count result)))))
