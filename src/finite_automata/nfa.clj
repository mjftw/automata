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
  "Input a value or sequence of values into the NFA to get a new NFA.
  Will also follow any available free moves"
  (let [values (u/conform-seq value) {:keys [current-states rules accept-states]} nfa]
    (if (seq values)
      (reduce
       (fn [current-nfa next-value]
         (->NFA
          (->>
           next-value
           (r/next-states rules (:current-states current-nfa))
           (r/follow-free-moves rules))
          accept-states
          rules))
       nfa
       values)
      (->NFA (r/follow-free-moves rules current-states) accept-states rules))))

(defn accepts-input? [nfa values]
  (accepting? (input nfa values)))