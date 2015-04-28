(ns guten.core
  (:require [opennlp.nlp :refer :all]))

(def tokenize (make-tokenizer "resources/en-token.bin"))

(def guten
  (slurp "resources/guten.txt"))

(defn clean-text
  [text-str]
  (-> text-str
      (clojure.string/replace #"http://www|.com" "")
      (clojure.string/replace #"[\.,!?\(\)\[\]\_\-\=\:\"]" "")
      (clojure.string/replace #"\r|\n|[0-9]|" "")
      clojure.string/lower-case
      (clojure.string/split #"\s+")))

(defn count-words 
  [text-str]
  (sort-by val > (frequencies (clean-text text-str))))


;;lemmatisation
