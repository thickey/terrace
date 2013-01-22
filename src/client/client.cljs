(ns terrace.client
  (:use [terrace.dom :only [domready watch data target q]]
        [webfui.dom :only [defdom]])
  (:require [terrace.reader :as reader]
            [terrace.views.tree :as tree]))

(def my-dom (atom nil))
(def state (atom []))
(def socket (.connect js/io "http://localhost"))

(defdom my-dom)

(defn render-all [old-dom]
  (if-not (empty? @state)
    [:div
     [:h1 "Received edn"]
     (tree/tree @state)]
    [:div
     [:h1 "Awaiting edn"]]))

(defn render-failure [old-dom]
  [:div [:h1.error "Error reading edn."]])

(defn update-dom
  ([dom-fn]
     (swap! my-dom dom-fn))
  ([]
     (update-dom render-all)))


(defn on-new-edn [edn]
  (let [data (reader/read edn)]
    (if (= :success (:read-status data))
      (do
        (.log js/console (clj->js data))
        (reset! state (:data data))
        (update-dom))
      (update-dom render-failure))))

;; (defn on-expand [event]
;;   (let [id (data (target event) "id")]
;;     (.emit socket "expand" id)))

;; (defn on-clear [event]
;;   (let [id (data (target event) "id")]
;;     (.emit socket "clear" id)))

(domready
 (fn []
   (update-dom)
   ;;(watch "click" "span.expand" on-expand)
   ;;(watch "click" "span.clear" on-clear)
   (.on socket "edn" on-new-edn)))
