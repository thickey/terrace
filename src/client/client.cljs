(ns terrace.client
  (:use [terrace.dom :only [domready watch data target q]]
        [webfui.dom :only [defdom]]))

(def my-dom (atom nil))
(def state (atom []))
(def socket (.connect js/io "http://localhost"))

(defdom my-dom)

(defn render-all [old-dom]
  (if-not (empty? @state)
    [:div
     [:h1
      "Received edn"]
     [:pre @state]]
    [:div
     [:h1 "Awaiting edn"]]))

(defn update-dom []
  (swap! my-dom render-all))

(defn on-new-edn [edn]
  (let [edn-str (:edn (js->clj edn))]
    (reset! state (str edn))
    (update-dom)))

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
