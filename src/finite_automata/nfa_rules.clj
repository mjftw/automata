(ns finite-automata.nfa-rules
  "Rules for Non-deterministic Finite Automata"
  (:require
   [clojure.set :as s]
   [finite-automata.utils :as u]
   [finite-automata.dfa-rules :as dfa]))

(defn next-states [rules states input]
  (->>
   (u/combinations rules states)
   (filter (fn [[rule state]] (dfa/rule-applies? rule state input)))
   (map (fn [[rule]] (dfa/follow rule)))
   set))

(defn follow-free-moves [rules states]
  (loop [states states]
    (let [states (set states)
          extra-states (set (next-states rules states :free-move))]
      (if (s/subset? extra-states states)
        states
        (recur (s/union states extra-states))))))