(ns terrace.server
  (:require-macros [hiccups.core :as hiccups])
  (:require [hiccups.runtime :as hiccupsrt]
            ;;[goog.string :as gstring]
            ))

(defn log [& args] (apply (.-log js/console) (map str args)))

(def http (js/require "http"))
(def express (js/require "express"))
(def socket-io (js/require "socket.io"))

(def coll (atom nil))

(defn page-template []
  (hiccups/html
   [:html
    [:head
     [:link {:rel "stylesheet" :href "/screen.css"}]
     [:script {:type "text/javascript" :src "/socket.io/socket.io.js"}]
     ;;[:script {:type "text/javascript" :src "/cljs/goog/base.js"}]
     [:script {:type "text/javascript" :src "/cljs.js"}]
     [:script {:type "text/javascript"} "goog.require('terrace.client');"]]
    [:body]]))

(defn get-main [req res]
  (.send res (page-template)))

;;; http://stackoverflow.com/questions/12345166/how-to-force-parse-request-body-as-plain-text-instead-of-json-in-express
(defn raw-body [req res nxt]
  (.setEncoding req "utf8")
  (set! (.-body req) "")
  (.on req "data" (fn [chunk]
                    ;;(.log js/console (str "chunk:" chunk))
                    (set! (.-body req) (str (.-body req) chunk))))
  (.on req "end" #(nxt)))
;; (defn raw-body [req res nxt]
;;   (let [sb (gstring/StringBuffer.)
;;         writer (StringBufferWriter. sb)]
;;     (.setEncoding req "utf8")
;;     (.on req "data" (fn [chunk]
;;                       (-write writer chunk)
;;                       (set! (.-body req) (str sb))))
;;     (.on req "end" (fn []
;;                      (-flush writer)
;;                      ;;(set! (.-body req) (str sb))
;;                      (nxt)))))

(defn receive-edn [socket req res]
  (if socket
    (try
      (.emit socket "edn" (.-body req))
      (.send res (.-body req))
      (catch js/Object e
         (.send res 500 (clj->js {:error "Unable to send edn over socket." }))))
    (.send res 500 (clj->js {:error "Socket unavailable." }))))

(defn server []
  (let [app (express)
        server (.createServer http app)
        io (.listen socket-io server)]
    #_(.use app (.logger express))
    ;; works, but for json only: (.use app (.bodyParser express))
    (.use app raw-body)
    (-> app
        (.use ((aget express "static") "static"))
        (.get "/" get-main))
    (.on (.-sockets io) "connection"
         (fn [socket]
           ;; (.on socket "expand" (partial on-expand socket))
           ;; (.on socket "clear" (partial on-clear socket))
           (-> app
               (.post "/edn" (partial receive-edn socket)))))
    (.listen server 1337))
  (log "listening on http://localhost:1337/"))

(defn main [& args]
  (server))

(set! *main-cli-fn* main)
