(ns automata.turing.machine
  "Functions for running a Turing Machine."
  (:require [automata.turing.config :as c]))

(defrecord Machine [state accept-states tape rules])

(defn config [machine]
  "Get the machine's configuration"
  (c/->TMConfig (:state machine) (:tape machine)))

(defn accepting? [machine]
  "Is the machine in an accept state?"
  (contains? (:accept-states machine) (:state machine)))

(defn stuck? [machine]
  "Is the machine stuck?
  I.e. Are there no rules that apply to the current configuration?"
  (nil? (c/rule-for (:rules machine) (config machine))))

(defn run [machine]
  "Run the machine until it reaches an accept state, or becomes stuck.
  Returns the stopped machine, or nil if the machine given has overlapping, and
  therefore non-deterministic rules."
  (if (not (c/rules-valid? (:rules machine)))
    nil
    (loop [machine machine]
      (cond
        (accepting? machine) machine
        (stuck? machine) machine
        :else (let [{:keys [tape rules accept-states]} machine
                    new-config (c/follow (c/rule-for rules (config machine)) tape)
                    new-machine (map->Machine
                                 {:state (:state new-config)
                                  :accept-states accept-states
                                  :tape (:tape new-config)
                                  :rules rules})]
                (recur new-machine))))))