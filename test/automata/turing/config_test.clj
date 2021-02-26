(ns automata.turing.config-test
  (:require [clojure.test :refer :all]
            [automata.turing.config :refer :all]))

(def tape (->Tape '(-1 -2 -3) ;left
                  0           ;head
                  '(1 2 3)))  ;right

(deftest test-move-head
  (testing "move-head-left! success when left position available"
    (is (= (->Tape '(-2 -3)
                   -1
                   '(0 1 2 3))
           (move-head-left! tape))))
  (testing "move-head-right! success when right position available"
    (is (= (->Tape '(0 -1 -2 -3)
                   1
                   '(2 3))
           (move-head-right! tape))))
  (testing "exception is thrown when trying to move left past end of tape"
    (is (thrown? Exception
                 (move-head-left! (->Tape '()
                                          0
                                          '(1 2 3))))))
  (testing "exception is thrown when trying to move right past end of tape"
    (is (thrown? Exception
                 (move-head-right! (->Tape '(-1 -2 3)
                                           0
                                           '()))))))

(deftest test-applies-to?
  (testing "returns true if state and tape head match rule"
    (is (true? (applies-to? (->TMConfig 1 tape)
                            (->TMRule 1 0 1 0 :left)))))
  (testing "returns false if state does not match rule"
    (is (false? (applies-to? (->TMConfig 1 tape)
                             (->TMRule 2 0 1 0 :left)))))
  (testing "returns false if tape head does not match rule"
    (is (false? (applies-to? (->TMConfig 1 tape)
                             (->TMRule 1 :foo 1 0 :left))))))