(ns hipstr.test.handler
  (:use clojure.test
        ring.mock.request
        hipstr.handler))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))

(deftest missing-email-address-redisplays-the-form
  (let [response (app (request :post "/signup" {:username "TheDude" :password "123456789"}))]
    (is (= 200 (:status response)))))
