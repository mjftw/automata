(ns automata.turing.tape-test
  (:require [clojure.test :refer :all]
            [automata.turing.tape :refer :all]))

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