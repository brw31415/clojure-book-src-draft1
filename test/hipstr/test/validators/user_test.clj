(ns hipstr.test.validators.user-test
  (:use clojure.test
        hipstr.validators.user))

(deftest user-validator-tests
  (testing "email validator"
    (testing "incorrect format tests"
          (let [result (:email (email-validator {:email "asdf"}))]
            (is (= 1 (count result)))
            (is (= "The email's format is incorrec" (first result)))))))
