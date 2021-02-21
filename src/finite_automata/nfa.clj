(ns finite-automata.nfa
  "Non-deterministic Finite Automata"
  (:require [clojure.set :as s]
            [finite-automata.utils :as u]
            [finite-automata.dfa :as dfa]
            [finite-automata.nfa-rules :as nr]
            [finite-automata.dfa-rules :as dr]))

(defrecord NFA [current-states accept-states rules])

(defn accepting? [nfa]
  (let [current-states (set (:current-states nfa))
        accept-states (set (:accept-states nfa))]
    (not (empty? (s/intersection current-states accept-states)))))

(defn input [nfa value]
  "Input a value or sequence of values into the NFA to get a new NFA.
  Will also follow any available free moves"
  (let [values (u/conform-seq value) {:keys [current-states rules accept-states]} nfa]
    (if (seq values)
      (reduce
       (fn [current-nfa next-value]
         (->NFA
          (->>
           next-value
           (nr/next-states rules (:current-states current-nfa))
           (nr/follow-free-moves rules))
          accept-states
          rules))
       nfa
       values)
      (->NFA (nr/follow-free-moves rules current-states) accept-states rules))))

(defn accepts-input? [nfa values]
  (accepting? (input nfa values)))


;; The issue with the current simulation is that free moves are not followed,
;;  resulting in a dfa that doesn't jump to free move states when the NDA equivalent does.
;;  This means that the NFA occasionally is able to reach an extra state over the DFA
;;  Trying to fix this. Other than this issue they are equivalent.


(defn discover-states-and-rules [nfa]
  "Convert a non-deterministic finite automata to its deterministic equivalent"
  (let [possible-inputs (set (map :input (:rules nfa)))]
    (loop [known-states #{(set (:current-states nfa))}]
      (let [rules (apply s/union
                         (map #(nr/rules-for (:rules nfa) % possible-inputs) known-states))
            next-states (->> rules (map #(dr/follow %)) (map #(nr/follow-free-moves (:rules nfa) %)) set)]
        (if (s/subset? next-states known-states)
          [known-states (seq rules)]
          (recur (s/union known-states next-states)))))))

(defn to-dfa [nfa]
  "Convert a non-deterministic finite automata to its deterministic equivalent"
  (let [[states rules] (discover-states-and-rules nfa)]
    (dfa/->DFA
     (:current-states nfa)
     (set (filter #(accepting? (->NFA % (:accept-states nfa) #{})) states))
     rules)))