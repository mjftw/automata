(ns automata.finite.dfa
  "Deterministic Finite Automata (finite state machine)"
  (:require [automata.finite.dfa-rules :as r]
            [automata.utils :as u]))

(defrecord DFA [current-state accept-states rules])

(defn accepting? [dfa]
  (contains? (set (:accept-states dfa)) (:current-state dfa)))

(defn input [dfa value]
  "Input a value or sequence of values into the DFA to get a new DFA"
  (let [values (u/conform-seq value) {:keys [rules accept-states]} dfa]
    (reduce
     (fn [current-dfa next-value]
       (->DFA
        (r/next-state rules (:current-state current-dfa) next-value)
        accept-states
        rules))
     dfa
     values)))

(defn accepts-input? [dfa values]
  (accepting? (input dfa values)))