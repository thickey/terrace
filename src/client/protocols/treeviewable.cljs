(ns terrace.protocols.treeviewable)

(defprotocol ITreeViewable
  (collapsed-tree-view [this])
  (expanded-tree-view [this]))
