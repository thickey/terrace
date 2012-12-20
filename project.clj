(defproject terrace "0.1.0-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "0.2.10"]]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojurescript "0.0-1552"]
                 [hiccups "0.1.1"]
                 [webfui "0.2"]]
  :cljsbuild {:builds
              [{:source-path "src/server"
                :compiler {:output-to "js/main.js"
                           :output-dir "js"
                           :optimizations :simple
                           :target :nodejs}}
               {:source-path "src/client"
                :compiler {:output-to "static/cljs.js"
                           :output-dir "static/cljs"}}]})
