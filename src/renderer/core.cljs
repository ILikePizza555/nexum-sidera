(ns renderer.core
  (:require [reagent.core :refer [atom as-element]]
            [reagent.dom :as rd]
            ["react-dom/client" :refer [createRoot]]))

(enable-console-print!)

(defonce root (createRoot (js/document.getElementById "app-container")))
(defonce state (atom 0))

(defn tree-view-item [{:keys [label]} & children]
  [:li.tree-view-item 
   [:div 
    [:span.icon-container (if children "\u25B9" ())]
    [:span.label label]]
   (if children [:ul.tree-view-group children] ())])

(defn root-component []
  [:ul.tree-view-group.tree-view-root
   [tree-view-item {:label "Leaf Component"}]
   [tree-view-item {:label "Subtree"}
    [tree-view-item {:label "Leaf 1"}]
    [tree-view-item {:label "Leaf 2"}]
    [tree-view-item {:label "Leaf 3"}]]
   [tree-view-item {:label "Subtree with sub-subtree"}
    [tree-view-item {:label "Leaf 1"}]
    [tree-view-item {:label "Sub-subtree"}
     [tree-view-item {:label "Leaf 2"}]
     [tree-view-item {:label "Leaf 3"}]
     [tree-view-item {:label "Leaf 4"}]]
    [tree-view-item {:label "Leaf 5"}]]])

(defn ^:dev/after-load start! []
  (.render root (as-element [root-component])))