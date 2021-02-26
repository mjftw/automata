(ns automata.core
  (:require [automata.utils :refer :all]
            [automata.finite.dfa-rules :as dr]
            [automata.finite.nfa-rules :as nr]
            [automata.finite.dfa :as dfa]
            [automata.finite.nfa :as nfa]
            [automata.turing.config :as tmc]
            [automata.turing.machine :as tm]))
