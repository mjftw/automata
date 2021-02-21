(ns finite-automata.nfa-rules-test
  (:require
   [clojure.test :refer :all]
   [finite-automata.nfa-rules :refer :all]
   [finite-automata.dfa-rules :as dfa]))

(def rules [(dfa/->FARule 1 "a" 1)
            (dfa/->FARule 1 "b" 1)
            (dfa/->FARule 1 "b" 2)
            (dfa/->FARule 2 "a" 3)
            (dfa/->FARule 2 "b" 3)
            (dfa/->FARule 4 :free-move 3)
            (dfa/->FARule 5 :free-move 4)])

(deftest test-next-states
  (testing "correct next state when only one possibility"
    (is (= #{1} (next-states rules #{1} "a"))))
  (testing "correct next state when multiple possibilities"
    (is (= #{1 2} (next-states rules #{1} "b"))))
  (testing "correct next state when multiple start states"
    (is (= #{1 3} (next-states rules #{1 2} "a")))))

(deftest test-follow-free-moves
  (testing "no change when no available free moves"
    (is (= #{1 2} (follow-free-moves rules #{1 2}))))
  (testing "follows available free moves"
    (is (= #{1 3 4 5} (follow-free-moves rules #{1 5})))))

(deftest test-rules-for
  (testing "returns rules that apply with one input"
    (is (= #{(dfa/->FARule 1 "b" 1) (dfa/->FARule 1 "b" 2)}
           (rules-for rules 1 ["b"]))))
  (testing "returns rules that apply with multiple inputs"
    (is (= #{(dfa/->FARule 2 "a" 3) (dfa/->FARule 2 "b" 3)}
           (rules-for rules 2 ["a" "b"]))))
  (testing "returns empty set when no inputs"
    (is (= #{} (rules-for rules 2 []))))
  (testing "returns empty set when no rule with state"
    (is (= #{} (rules-for rules 10 ["a"]))))
  (testing "returns empty set when no rule with input"
    (is (= #{} (rules-for rules 2 ["c"])))))