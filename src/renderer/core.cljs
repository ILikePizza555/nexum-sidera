(ns renderer.core
  (:require [helix.core :refer [defnc $]]
            [helix.dom :as d]
            ["react-dom/client" :as rdom]
            [dv.cljs-emotion :refer [defstyled]]))

(enable-console-print!)

(defstyled tree-root :ul
  {:list-style-type "none"
   :margin 0
   :padding 0})

(defstyled tree-content :ul
  {:list-style-type "none"
   :margin 0
   :padding-inline-start "0.7em"})

(defnc tree [{:keys [label children]}]
  (d/li
   (d/div
    (d/span "Icon Container")
    (d/span label))
   (when children (tree-content children))))

(defnc app []
  (tree-root
   ($ tree {:label "🍃 Leaf Component"})
   ($ tree {:label "🌲 Subtree"}
      ($ tree {:label "Leaf 1"})
      ($ tree {:label "Leaf 2"})
      ($ tree {:label "Leaf 3"}))
   ($ tree {:label "🎋 Subtree with sub-subtree"}
      ($ tree {:label "Leaf 1"})
      ($ tree {:label "Sub-subtree"}
         ($ tree {:label "Leaf 2"})
         ($ tree {:label "Leaf 3"})
         ($ tree {:label "Leaf 3"}))
      ($ tree {:label "Leaf 5"}))))

(defonce root (rdom/createRoot (js/document.getElementById "app-container")))
(defn ^:dev/after-load start! []
  (.render root ($ app)))