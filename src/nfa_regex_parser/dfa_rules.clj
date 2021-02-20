(ns nfa-regex-parser.dfa-rules)

(defrecord FARule [state character next-state])

(defn rule-applies? [rule state character]
  (and (= state (:state rule)) (= character (:character rule))))

(defn follow [rule] (:next-state rule))

(defn rule-for [rules state character]
  (some #(when (rule-applies? % state character) %)
        rules))

(defn next-state [rules state character]
  (follow (rule-for rules state character)))
