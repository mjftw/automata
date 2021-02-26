(ns automata.finite.nfa
  "Non-deterministic Finite Automata"
  (:require [clojure.set :as s]
            [automata.utils :as u]
            [automata.finite.dfa :as dfa]
            [automata.finite.nfa-rules :as nr]
            [automata.finite.dfa-rules :as dr]))

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
