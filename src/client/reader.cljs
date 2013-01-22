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
  (try
    (let [data (reader/read-string s)]
      {:read-status :success
       :data data})
       (catch js/Object e
         (.log js/console e)
         {:read-status :failure
          :message "Error reading edn."})))
