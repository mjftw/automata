(ns finite-automata.utils)

(defn combinations [seq1 seq2]
  (apply concat
         (map (fn [a]
                (map (fn [b]
                       [a b])
                     seq2))
              seq1)))