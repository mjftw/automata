(ns finite-automata.nfa-test
  (:require [clojure.test :refer :all]
            [finite-automata.nfa :refer :all]
            [finite-automata.dfa-rules :as r]))

(def nfa (->NFA [1] [4] [(r/->FARule 1 "a" 1)
                         (r/->FARule 1 "b" 1)
                         (r/->FARule 1 "b" 2)
                         (r/->FARule 2 "a" 3)
                         (r/->FARule 2 "b" 3)
                         (r/->FARule 3 "a" 4)
                         (r/->FARule 3 "b" 4)]))

(deftest test-accepting?
  (testing "should return true when NFA in an accept state"
    (is (true? (accepting? (->NFA [1 2] [2 3] [])))))
  (testing "should return false when NFA not in an accept state"
    (is (false? (accepting? (->NFA [1 2] [4] []))))))

(deftest test-input
  (let [{:keys [accept-states rules]} nfa]
    (testing "returns NFA with current-state updated when given a single value"
      (is (= (->NFA [1] accept-states rules) (input nfa "a"))))
    (testing "returns NFA with current-state updated when given a sequence of values"
      (is (= (->NFA [1 4] accept-states rules) (input nfa ["b" "a" "a"]))))
    (testing "returns NFA with current-state updated when given a string"
      (is (= (->NFA [1 4] accept-states rules) (input nfa "baa"))))))
