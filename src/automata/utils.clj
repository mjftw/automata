(ns automata.utils)

(defn combinations [seq1 seq2]
  (apply concat
         (map (fn [a]
                (map (fn [b]
                       [a b])
                     seq2))
              seq1)))

(defn cross-map [f coll]
  "Find all pairwise combinations of a collection, and apply f to each pair.
  Pairs will not include comparing an element to itself"
  (for [a-and-idx (map list coll (range))
        b-and-idx (map list coll (range))
        :let [a (first a-and-idx)
              b (first b-and-idx)
              a-idx (second a-and-idx)
              b-idx (second b-and-idx)]
        :when (not= a-idx b-idx)]
    (f a b)))

(defn conform-seq [value]
  "Convert value to lazy sequence. String goes to sequence of char strings"
  (cond
    (not (seqable? value)) '(value)
    (string? value) (map str (seq value))
    :else (lazy-seq value)))