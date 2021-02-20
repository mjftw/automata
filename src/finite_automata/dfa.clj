(ns finite-automata.dfa
  "Deterministic Finite Automata (finite state machine)"
  (:require [finite-automata.dfa-rules :as r]))

(defrecord DFA [current-state accept-states rules])

(defn accepting? [dfa]
  (contains? (set (:accept-states dfa)) (:current-state dfa)))

(defn- conform-seq [value]
  "Convert value to lazy sequence. String goes to sequence of char strings"
  (cond
    (not (seqable? value)) '(value)
    (string? value) (map str (seq value))
    :else (lazy-seq value)))

(defn input [dfa value]
  "Input a value or sequence of values into the DFA to get a new DFA"
  (let [values (conform-seq value) {:keys [rules accept-states]} dfa]
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