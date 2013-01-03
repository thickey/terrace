(ns terrace.protocols.treemodel)

(defprotocol ITreeModel
  (is-leaf? [this])
  (child-at [this idx])
  (child-count [this]))
