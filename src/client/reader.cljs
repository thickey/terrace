(ns terrace.reader
  (:require [cljs.reader :as reader]))

(reader/register-tag-parser!
 "terrace/lazy"
 (fn [uuid]
   (let [u (UUID. uuid)]
     (.log js/console uuid)
     u)))

;; (swap! reader/*tag-table*
;;        assoc "terrace/lazy"
;;        (fn [uuid]
;;          (let [u (UUID. uuid)]
;;            (.log js/console uuid)
;;            u)))

(defn read
  [s]
  (try (reader/read-string s)
       (catch js/Object e
         (.log js/console e)
         "Error reading edn.")))
