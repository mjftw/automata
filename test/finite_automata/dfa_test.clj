(ns finite-automata.dfa-test
  (:require [clojure.test :refer :all]
            [finite-automata.dfa :refer :all]
            [finite-automata.dfa-rules :as r]))

(def dfa
  "A DFA that accepts the strings '' and 'abba'"
  (->DFA 1 [1] [(r/->FARule 1 "a" 2)
                (r/->FARule 1 "b" 1)
                (r/->FARule 2 "a" 2)
                (r/->FARule 2 "b" 3)
                (r/->FARule 3 "a" 3)
                (r/->FARule 3 "b" 4)
                (r/->FARule 4 "a" 1)
                (r/->FARule 4 "b" 4)]))

(deftest test-accepting?
  (testing "returns true when in accept state"
    (is (true? (accepting? (->DFA 1 [1] [])))))
  (testing "returns false when not in accept state"
    (is (false? (accepting? (->DFA 1 [2 3] []))))))

(deftest test-input
  (let [{:keys [accept-states rules]} dfa]
    (testing "returns DFA with current-state updated when given a single value"
      (is (= (->DFA 2 accept-states rules) (input dfa "a"))))
    (testing "returns DFA with current-state updated when given a sequence of values"
      (is (= (->DFA 4 accept-states rules) (input dfa ["a" "b" "b"]))))
    (testing "returns DFA with current-state updated when given a string"
      (is (= (->DFA 4 accept-states rules) (input dfa "abb"))))))