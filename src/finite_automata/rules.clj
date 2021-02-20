(ns finite-automata.rules
  "Rules for Deterministic and Non-deterministic Finite Automata"
  (:require [finite-automata.utils :as u]))

(defrecord FARule [state input next-state])

(defn rule-applies? [rule state input]
  (and (= state (:state rule)) (= input (:input rule))))

(defn follow [rule] (:next-state rule))

(defn rule-for [rules state input]
  (some #(when (rule-applies? % state input) %)
        rules))

(defn next-state [rules state input]
  (follow (rule-for rules state input)))

;; NFA

(defn next-states [rules states input]
  (->>
   (u/combinations rules states)
   (filter (fn [[rule state]] (rule-applies? rule state input)))
   (map (fn [[rule]] (follow rule)))
   set
   seq))

