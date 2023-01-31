(ns renderer.core
  (:require [reagent.core :refer [atom as-element]]
            [reagent.dom :as rd]
            ["react-dom/client" :refer [createRoot]]))

(enable-console-print!)

(defonce root (createRoot (js/document.getElementById "app-container")))
(defonce state (atom 0))

(defn root-component []
  [:ul.tree-view.tree-view-group.tree-view-root
   [:li.tree-view.tree-view-item "Leaf Component"]
   [:li.tree-view.tree-view-group "Subtree"
    [:ul
     [:li "Leaf 1"]
     [:li "Leaf 2"]
     [:li "Leaf 3"]]]
   [:li.tree-view "Subtree with sub-subtree"
    [:ul
     [:li "Leaf 1"]
     [:li "Sub-subtree"
      [:ul
       [:li "Leaf 2"]
       [:li "Leaf 3"]
       [:li "Leaf 4"]]]
     [:li "Leaf 5"]]]])

(defn ^:dev/after-load start! []
  (.render root (as-element [root-component])))