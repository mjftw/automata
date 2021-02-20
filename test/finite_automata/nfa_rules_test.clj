(ns finite-automata.dfa-rules-test
  (:require
   [clojure.test :refer :all]
   [finite-automata.nfa-rules :refer :all]
   [finite-automata.dfa-rules :as dfa]))

(def rules [(dfa/->FARule 1 "a" 1)
            (dfa/->FARule 1 "b" 1)
            (dfa/->FARule 1 "b" 2)
            (dfa/->FARule 2 "a" 3)
            (dfa/->FARule 2 "b" 3)])

(deftest test-next-states
  (testing "correct next state when only one possibility"
    (is (= [1] (next-states rules [1] "a"))))
  (testing "correct next state when multiple possibilities"
    (is (= [1 2] (next-states rules [1] "b"))))
  (testing "correct next state when multiple start states"
    (is (= [1 3] (next-states rules [1 2] "a")))))