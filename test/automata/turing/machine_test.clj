(ns automata.turing.machine-test
  (:require [clojure.test :refer :all]
            [automata.turing.machine :refer :all]
            [automata.turing.config :refer :all]))

(def machine
  "This machine performs binary number increments"
  (map->Machine
   {:state :a
    :accept-states #{:c}
    :tape (->Tape '(1 0 1) 1 '())
    :rules #{(->TMRule :a 1   :a 0   :left)
             (->TMRule :a 0   :b 1   :right)
             (->TMRule :a nil :b 1   :right)
             (->TMRule :b 0   :c 0   :right)
             (->TMRule :b 1   :c 1   :right)
             (->TMRule :b nil :c nil :left)}}))

(deftest test-accepting?
  (testing "returns true if machine in an accept state"
    (is (true? (accepting? (->Machine 1 #{1} (->Tape '() :? '()) #{})))))
  (testing "returns false if machine not in an accept state"
    (is (false? (accepting? (->Machine 1 #{2} (->Tape '() :? '()) #{}))))))

(deftest test-stuck?
  (testing "returns true if no rules apply to current state and tape"
    (is (true? (stuck? (->Machine :a
                                  #{:b}
                                  (->Tape '() 1 '())
                                  #{(->TMRule :a 2 :? :? :?)})))))
  (testing "returns false if rules apply to current state and tape"
    (is (false? (stuck? (->Machine :a
                                   #{:b}
                                   (->Tape '() 1 '())
                                   #{(->TMRule :a 1 :? :? :?)}))))))

(deftest test-run
  (testing "Binary incrementing machine does its job.
    101 -> 110 (read left tape backwards)."
    (is (= (->Tape '(0 1 1) 0 '())
           (:tape (run machine)))))
  (testing "Binary incrementing machine ends in accept state"
    (is (accepting? (run machine)))))