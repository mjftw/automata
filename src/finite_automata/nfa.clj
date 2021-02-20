(ns finite-automata.dfa
  "Non-deterministic Finite Automata"
  (:require [clojure.set :as s]
            [finite-automata.dfa-rules :as r]))

(defrecord NFA [current-states accept-states rules])

(defn accepting? [nfa]
  (let [current-states (set (:current-states nfs))
        accept-states (set (:accept-states nfs))]
    (not (empty? (s/intersection current-states accept-states)))))