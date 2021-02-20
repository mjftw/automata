(ns nfa-regex-parser.dfa-rules)

(defrecord FARule [state input next-state])

(defn rule-applies? [rule state input]
  (and (= state (:state rule)) (= input (:input rule))))

(defn follow [rule] (:next-state rule))

(defn rule-for [rules state input]
  (some #(when (rule-applies? % state input) %)
        rules))

(defn next-state [rules state input]
  (follow (rule-for rules state input)))
