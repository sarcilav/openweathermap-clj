(ns open-weather.core-test
  (:require [clojure.test :refer :all]
            [open-weather.core :refer :all]))

(def env {:open-weather-key "XXXX your key XXXX"})

(deftest base-url-test
  (testing "building the base-url to open weather"
    (let [topic "weather"
          expected (str "http://api.openweathermap.org/data/2.5/weather")]
      (is (= expected (base-url topic))))))

(deftest create-query-string-test
  (testing "create-query-string generator is using the proper separators"
    (let [keys ["a" "b" "c" "d"]
          keys2 [:a :b :c :d]
          queries [1 2 3 4]
          queries2 [1.1 2.2 3.3 4.4]
          expected ["a=1" "b=2" "c=3" "d=4"]
          expected2 ["a=1.1" "b=2.2" "c=3.3" "d=4.4"]]
      (is (= expected (create-query-string (zipmap keys2 queries))))
      (is (= expected (create-query-string (zipmap keys queries))))
      (is (= expected2 (create-query-string (zipmap keys queries2)))))))

(deftest build-url-test
  (testing "build-url that attach the query to end of a base url"
    (let [base "example.com"
          query {"hello" "sample"
                 "a" "b"}
          expected (str base "?hello=sample&a=b")]
      (is (= expected (build-url base query))))))

(deftest weather-at-test
  (let [api-key (env :open-weather-key)
        berlin-weather (weather-at "berlin" api-key)
        geo-weather (weather-at 3.24 -70.0 api-key)
        bad-query (weather-at 30000 30000 api-key)]
    (testing "query to get a city weather report"
      (is (= 200 (:cod berlin-weather)))
      (is (= "Berlin" (:name berlin-weather))))
    (testing "query to get lat/lon weather report"
      (is (= 200 (:cod geo-weather)))
      (is (= "Chananané" (:name geo-weather))))
    (testing "bad query"
      (is (= 400 (:cod bad-query))))))
