(ns automata.turing.core-test
  (:require [clojure.test :refer :all]
            [automata.turing.core :refer :all]))

(def tape (->Tape '(-1 -2 -3) ;left
                  0           ;head
                  '(1 2 3)))  ;right

(deftest test-move-head
  (testing "move-head-left success when left position available"
    (is (= (->Tape '(-2 -3)
                   -1
                   '(0 1 2 3))
           (move-head-left tape))))
  (testing "move-head-right success when right position available"
    (is (= (->Tape '(0 -1 -2 -3)
                   1
                   '(2 3))
           (move-head-right tape)))))