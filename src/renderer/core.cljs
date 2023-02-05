(ns renderer.core
  (:require [helix.core :refer [defnc $]]
            [helix.dom :as d]
            ["react-dom/client" :as rdom]))

(enable-console-print!)

(defnc tree-view-item [{:keys [label children]}]
  (d/li
   (d/div
    (d/span "Icon Container")
    (d/span label))
   (when children (d/ul children))))

(defnc app []
  (d/ul
   ($ tree-view-item {:label "🍃 Leaf Component"})
   ($ tree-view-item {:label "🌲 Subtree"}
      ($ tree-view-item {:label "Leaf 1"})
      ($ tree-view-item {:label "Leaf 2"})
      ($ tree-view-item {:label "Leaf 3"}))
   ($ tree-view-item {:label "🎋 Subtree with sub-subtree"}
      ($ tree-view-item {:label "Leaf 1"})
      ($ tree-view-item {:label "Sub-subtree"}
         ($ tree-view-item {:label "Leaf 2"})
         ($ tree-view-item {:label "Leaf 3"})
         ($ tree-view-item {:label "Leaf 3"}))
      ($ tree-view-item {:label "Leaf 5"}))))

(defonce root (rdom/createRoot (js/document.getElementById "app-container")))
(defn ^:dev/after-load start! []
  (.render root ($ app)))