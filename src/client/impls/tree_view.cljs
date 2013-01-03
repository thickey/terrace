(ns terrace.impls.tree-view
  (:require [terrace.protocols.treemodel :as model]
            [terrace.protocols.treeviewable :as viewable]))

(defn atom? [x]
  (not (coll? x)))

(defn collection-tag [x]
  (cond
   (map? x) :map
   (vector? x) :vector
   (set? x) :set
   (list? x) :list
   (satisfies? IRecord x) (-> x meta :type)
   (sequential? x) :seq
   :else :atom))

(defn collection-bounds [x]
  (cond
   (map? x) ["{" "}"]
   (vector? x) ["[" "]"]
   (set? x) ["#{" "}"]
   (sequential? x) ["(" ")"]))

(defn bounded-coll-str [coll content]
  (let [[prefix suffix] (collection-bounds coll)]
    (str prefix content suffix)))

(defn collection-title [coll]
  [:h2.coll-title (str (bounded-coll-str coll "")
                       " ("
                       (model/child-count coll)
                       " items)")])

(extend-type default
  model/ITreeModel
  (is-leaf? [this]
    (atom? this))
  (child-at [this idx]
    (nth (seq this) idx))
  (child-count [this]
    (count this))
  viewable/ITreeViewable
  (collapsed-tree-view [this]
    (if (model/is-leaf? this)
      [:span (str this)]
      [:span (str (bounded-coll-str this "")
                  " ("
                  (model/child-count this)
                  " items)")]))
  (expanded-tree-view [this]
    (if (model/is-leaf? this)
      [:div (str this)]
      [:div (collection-title this)
       [:div.expanded-tree-view-items
        (map (fn [i]
               [:div.tree-view-item (viewable/collapsed-tree-view i)])
             this)]])))


(extend-type PersistentHashMap
  viewable/ITreeViewable
  (collapsed-tree-view [this]
    [:span (str "{" (apply str (interpose "&hellip;, " (keys this))) " &hellip;}")])
  (expanded-tree-view [this]
    [:div (collection-title this)
     [:div.expanded-tree-view-items
      (map (fn [[k v]]
             [:div.tree-view-map-entry
              [:span.tree-view-map-entry-key (viewable/collapsed-tree-view k)]
              [:span.tree-view-map-entry-val (viewable/collapsed-tree-view v)]])
            this)]]))
