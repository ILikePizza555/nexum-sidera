(ns renderer.core
  (:require [helix.core :refer [defnc $]]
            ["react-dom/client" :as rdom]
            [renderer.components.tree-view :refer [tree tree-root]]))

(enable-console-print!)

(defnc app []
  (tree-root
   ($ tree {:title "🍃 Leaf Component"})
   ($ tree {:title "🌲 Subtree"}
      ($ tree {:title "Leaf 1"})
      ($ tree {:title "Leaf 2"})
      ($ tree {:title "Leaf 3"}))
   ($ tree {:title "🎋 Subtree with sub-subtree"}
      ($ tree {:title "Leaf 1"})
      ($ tree {:title "Sub-subtree"}
         ($ tree {:title "Leaf 2"})
         ($ tree {:title "Leaf 3"})
         ($ tree {:title "Leaf 3"}))
      ($ tree {:title "Leaf 5"}))))

(defonce root (rdom/createRoot (js/document.getElementById "app-container")))
(defn ^:dev/after-load start! []
  (.render root ($ app)))