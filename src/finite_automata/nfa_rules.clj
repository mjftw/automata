(ns finite-automata.nfa-rules
  "Rules for Non-deterministic Finite Automata"
  (:require
   [clojure.set :as s]
   [finite-automata.utils :as u]
   [finite-automata.dfa-rules :as dr]))

(defn next-states [rules states input]
  (->>
   (u/combinations rules states)
   (filter (fn [[rule state]] (dr/rule-applies? rule state input)))
   (map (fn [[rule]] (dr/follow rule)))
   set))

(defn follow-free-moves [rules states]
  (loop [states states]
    (let [states (set states)
          extra-states (set (next-states rules states :free-move))]
      (if (s/subset? extra-states states)
        states
        (recur (s/union states extra-states))))))

(defn rules-for [rules state inputs]
  (->>
   (u/combinations rules inputs)
   (filter (fn [[rule input]] (dr/rule-applies? rule state input)))
   (map first)
   set))