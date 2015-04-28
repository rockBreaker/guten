(ns guten.core
  (:require [opennlp.nlp :refer :all]))

(def guten
  (slurp "resources/guten.txt"))

(defn clean-text
  [text-str]
  (-> text-str
      (clojure.string/replace #"http://www|.com" "")
      (clojure.string/replace #"[\.,!?\(\)\[\]\_\-\=\:\"]" "")
      (clojure.string/replace #"\r|\n|[0-9]|" "")
      clojure.string/lower-case
      ;;(clojure.string/split #"\s+")
      ))

(defn count-words 
  [text-coll]
  (sort-by val > (frequencies text-coll)))


;; Tokenization
(def tokenize (make-tokenizer "resources/en-token.bin"))

(def guten-token (tokenize guten))

(defn get-tokens [coll-tokenized]
  (filterv #(not (empty? %))
           (map #(clean-text %) guten-token)))

(def counts
  (count-words (get-tokens guten-token)))

;; Lemmatisation
