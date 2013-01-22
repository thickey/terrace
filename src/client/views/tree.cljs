(ns terrace.views.tree
  (:require [terrace.impls.tree-view :as tree-view]
           [terrace.protocols.treemodel :as model]
           [terrace.protocols.treeviewable :as viewable]))

(defn tree
  [data]
  [:div.tree-view-container
   [:div.tree-items-container
    (viewable/expanded-tree-view data)
    [:div (str "raw data:" data)]]])
