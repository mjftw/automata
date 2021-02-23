(ns automata.finite.nfa-test
  (:require [clojure.test :refer :all]
            [automata.finite.nfa :refer :all]
            [automata.finite.dfa-rules :as r]))

(def nfa (->NFA #{1} #{4} #{(r/->FARule 1 "a" 1)
                            (r/->FARule 1 "b" 1)
                            (r/->FARule 1 "b" 2)
                            (r/->FARule 2 "a" 3)
                            (r/->FARule 2 "b" 3)
                            (r/->FARule 3 "a" 4)
                            (r/->FARule 3 "b" 4)
                            (r/->FARule 4 "b" 4)
                            (r/->FARule 4 "a" 6)
                            (r/->FARule 5 :free-move 4)
                            (r/->FARule 6 :free-move 5)}))

(deftest test-accepting?
  (testing "should return true when NFA in an accept state"
    (is (true? (accepting? (->NFA #{1 2} #{2 3} #{})))))
  (testing "should return false when NFA not in an accept state"
    (is (false? (accepting? (->NFA #{1 2} #{4} #{}))))))

(deftest test-input
  (let [{:keys [accept-states rules]} nfa]
    (testing "returns NFA with current-state updated when given a single value"
      (is (= (->NFA #{1} accept-states rules) (input nfa "a"))))
    (testing "returns NFA with current-state updated when given a sequence of values"
      (is (= (->NFA #{1 4} accept-states rules) (input nfa ["b" "a" "a"]))))
    (testing "follows free moves when given no input"
      (is (= (->NFA #{4 5 6} accept-states rules) (input (->NFA #{6} accept-states rules) []))))
    (testing "follows free moves when input moves it into a free move state"
      (is (= (->NFA #{1 4 5 6} accept-states rules) (input nfa "baaa"))))))

(deftest test-accepts-input?
  (testing "returns true when no input and dfa in already in accept state"
    (is (true? (accepts-input? (->NFA #{1 3} #{1} #{}) []))))
  (testing "returns true when no input and can get to accept state via free moves"
    (is (true? (accepts-input? (->NFA #{6} #{4} (:rules nfa)) []))))
  (testing "returns true when input moves NFA into an accept state"
    (is (true? (accepts-input? nfa "baa"))))
  (testing "returns false when input does not move NFA into an accept state"
    (is (false? (accepts-input? nfa "aabb")))))