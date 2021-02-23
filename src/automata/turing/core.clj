(ns automata.turing.core
  "Turing machine implementation")

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
;; left tape       <- - - -
;; tape head      |
;; right tape      - - ->
;;
;; == Move head left ==
;; left tape       - - ->
;; tape head      |
;; right tape      <- - - -
(defrecord Tape [left head right])

(defn move-head-left [tape]
  (let [{:keys [left head right]} tape]
    (->Tape (rest left)
            (first left)
            (cons head right))))

(defn move-head-right [tape]
  (let [{:keys [left head right]} tape]
    (->Tape (cons head left)
            (first right)
            (rest right))))