(ns finite-automata.dfa-test
  (:require [clojure.test :refer :all]
            [finite-automata.nfa :refer :all]
            [finite-automata.dfa-rules :as dfar]))

(deftest accepting?
  (testing "should return true when NFA in an accept state"
    (is (true? (accepting? (->NFA [1 2] [2 3] [])))))
  (testing "should return false when NFA not in an accept state"
    (is (true? (accepting? (->NFA [1 2] [4] []))))))