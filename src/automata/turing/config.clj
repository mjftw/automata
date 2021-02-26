(ns automata.turing.config
  "Turing Machine configuration includes the tape, rules, and state"
  (:require [automata.utils :as u]))

;; Represents the infinite tape a turing machine uses to as input and output.
;; For efficiency the tape here consists of two lists and a tape head value. The
;; The tape head position is moved left or right by pushing the current head value
;; onto one tape, and replacing it with the popped head of the other.
;; Values are always pushed and popped from the leftmost position on the left and
;; right tapes. You can think of the tape being folded in on itself, with the head
;; position at the fold. In this analogy, moving the head is the same as rolling
;; the fold to the left or right.
;;
;; Examples:
;;
;; == Move head right ==
;; left tape       <- - -
;; tape head      |
;; right tape      - - - ->
;;
;; == Move head left ==
;; left tape       - - - ->
;; tape head      |
;; right tape      <- - -
(defrecord Tape [left head right])

;; A rule defines what value should be read from the tape for the machine to move
;; from one state to the next, what it should write to the tape before moving,
;; and what direction along the tape it should direction (:left, :right)
(defrecord TMRule [state tape-read next-state tape-write direction])

;; A Configuration is a container wrapping the current state and current tape
(defrecord TMConfig [state tape])

(defn move-head-left [tape]
  "Move the tape head one position to the left. A blank tape value is represented
  by nil. A blank is added to the tape if no left position is available."
  (let [{:keys [left head right]} tape]
    (->Tape (rest left)
            (first left)
            (cons head right))))

(defn move-head-right [tape]
  "Move the tape head one position to the right. A blank tape value is represented
  by nil. A blank is added to the tape if no right position is available."
  (let [{:keys [left head right]} tape]
    (->Tape (cons head left)
            (first right)
            (rest right))))

(defn move [tape direction]
  "Move the tape head :left or :right.
  Do nothing for incorrect tape direction"
  (case direction
    :left (move-head-left tape)
    :right (move-head-right tape)
    tape))

(defn write [tape value]
  "Write a value to the tape"
  (assoc tape :head value))

(defn applies-to? [rule config]
  "Does this rule apply to this configuration?"
  (and (= (:state rule) (:state config))
       (= (:tape-read rule) (:head (:tape config)))))

(defn follow [rule tape]
  "Get the configuration resulting from following a given rule on a given tape"
  (let [{:keys [direction next-state] value :tape-write} rule]
    (map->TMConfig {:state next-state
                    :tape (-> tape
                              (write value)
                              (move direction))})))

(defn rules-valid? [rules]
  "Are the rules valid for a deterministic machine?
  I.e. Is there only ever one rule that could apply to a given configuration?"
  (apply (partial = false)
         (u/cross-map
          #(and (= (:state %1) (:state %2))
                (= (:tape-read %1) (:tape-read %2)))
          rules)))

(defn rule-for [rules config]
  "Get the rule that applies to the current configuration?
  Return nil if no rules apply."
  (some #(when (applies-to? % config) %) rules))
