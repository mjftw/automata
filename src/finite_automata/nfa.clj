(ns finite-automata.nfa
  "Non-deterministic Finite Automata"
  (:require [clojure.set :as s]
            [finite-automata.utils :as u]
            [finite-automata.nfa-rules :as r]))

(defrecord NFA [current-states accept-states rules])

(defn accepting? [nfa]
  (let [current-states (set (:current-states nfa))
        accept-states (set (:accept-states nfa))]
    (not (empty? (s/intersection current-states accept-states)))))

(defn input [nfa value]
  "Input a value or sequence of values into the NFA to get a new NFA"
  (let [values (u/conform-seq value) {:keys [rules accept-states]} nfa]
    (reduce
     (fn [current-nfa next-value]
       (->NFA
        (r/next-states rules (:current-states current-nfa) next-value)
        accept-states
        rules))
     nfa
     values)))