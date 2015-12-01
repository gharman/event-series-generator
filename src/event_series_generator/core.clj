(ns event-series-generator.core
  (:gen-class))

;;;; Create a series of events in which a person pushes a button.
;;;; Person and button objects will be represented as unique symbols.
;;;; Times will be ordered integers, with time 0 being the epoch for the data set.

;;;; Usage: (into-csv "/tmp/test.csv" (generate-event-sequence 8 2 3))


(defn person
  "Generate a new, unique person id"
  []
  (gensym "person"))

(defn button
  "Generate a new, unique button id"
  []
  (gensym "button"))

(defn event
  "Create an event: a person pushes a button at a time"
  [person button time]
  {:person person :button button :time time})

(defn generate-event-sequence
  "Generate a sequence of new events. A person may push more than one button at one time."
  [num-events num-people num-buttons]
  (let [buttons (take num-buttons (repeatedly button))
        people (take num-people (repeatedly person))]
    (concat (for [person people evnum (range (/ num-events num-people))]
              (event person (rand-nth buttons)  ; Note this will not prevent a person from going back to the same place
                     (rand-int num-events))))))

(defn into-csv
  "Store an event sequence into a CSV"
  [filename ev-seq]
  (doseq [event ev-seq]
    (spit filename (str (:person event) "," (:button event) "," (:time event) "\n") :append true)))


(defn -main
  [filename num-events num-people num-buttons]
  (into-csv filename (generate-event-sequence (Integer/parseInt num-events) (Integer/parseInt num-people) (Integer/parseInt num-buttons))))
