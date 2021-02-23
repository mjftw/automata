(ns automata.finite.utils)

(defn combinations [seq1 seq2]
  (apply concat
         (map (fn [a]
                (map (fn [b]
                       [a b])
                     seq2))
              seq1)))

(defn conform-seq [value]
  "Convert value to lazy sequence. String goes to sequence of char strings"
  (cond
    (not (seqable? value)) '(value)
    (string? value) (map str (seq value))
    :else (lazy-seq value)))