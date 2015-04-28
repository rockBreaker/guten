(ns guten.core
  (:require [opennlp.nlp :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Removing extra Gutenberg text metadata
;; using code from another dojo project:
;; https://github.com/kornysietsma/markov-dojo

(defn file-lines [f]
  (-> (slurp f)
      (clojure.string/split #"\r?\n")))

(def gutenprefix "*** START OF THIS PROJECT GUTENBERG EBOOK")
(def gutensuffix "*** END OF THIS PROJECT GUTENBERG EBOOK")

(defn prune-gutenguff [file-lines]
  (->> file-lines
       (drop-while #(not (.startsWith % gutenprefix)))
       rest
       (take-while #(not (.startsWith % gutensuffix)))))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; Helper functions:

(defn clean-text
  [text-str]
  (-> text-str
      (clojure.string/replace #"http://www|.com" "")
      (clojure.string/replace #"[\.,!\?\(\)\[\]\_\-;\+\=:\"]" "")
      (clojure.string/replace #"\r|\n|[0-9]" "")
      clojure.string/lower-case))

(defn count-words 
  [text-coll]
  (sort-by val > (frequencies text-coll)))


;; Tokenization:
;; Get a clean collection of words

(defn coll-lines [text-file]
  (apply str (prune-gutenguff
              (file-lines text-file))))
;; example: (coll-lines "resources/alice.txt")

(def tokenize (make-tokenizer "resources/en-token.bin"))

(defn get-tokens [text-lines]
  (->> text-lines
   tokenize
   (map #(clean-text %))
   (filterv #(not (empty? %)))))
;; example: (get-tokens (coll-lines "resources/alice.txt")) 

(defn counts [tokens]
  (count-words tokens))
;; example: (counts (get-tokens (coll-lines "resources/alice.txt")))


;; Lemmatisation / Stemmisation:
;; Only count once nouns or verbs
;; that have the same root
