(ns automata.finite.dfa-rules
  "Rules for Deterministic Finite Automata (finite state machines)")

(defrecord FARule [state input next-state])

(defn rule-applies? [rule state input]
  (and (= state (:state rule)) (= input (:input rule))))

(defn follow [rule] (:next-state rule))

(defn rule-for [rules state input]
  (some #(when (rule-applies? % state input) %)
        rules))

(defn rules-for-state? [rules state]
  (not (empty? (filter #(or (= state (:state %)) (= state (:next-state %))) rules))))

(defn rules-for-input? [rules input]
  (not (empty? (filter #(= input (:input %)) rules))))

(defn next-state [rules state input]
  (follow (rule-for rules state input)))

