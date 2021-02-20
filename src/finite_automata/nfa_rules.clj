(ns finite-automata.nfa-rules
  "Rules for Non-deterministic Finite Automata"
  (:require
   [finite-automata.utils :as u]
   [finite-automata.dfa-rules :as dfa]))

(defn next-states [rules states input]
  (->>
   (u/combinations rules states)
   (filter (fn [[rule state]] (dfa/rule-applies? rule state input)))
   (map (fn [[rule]] (dfa/follow rule)))
   set
   seq))
