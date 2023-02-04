(ns renderer.core
  (:require [reagent.core :refer [atom as-element]]
            [reagent.dom :as rd]
            ["react-dom/client" :refer [createRoot]]))

(enable-console-print!)

(defonce root (createRoot (js/document.getElementById "app-container")))

(defn collapse [is-collapsed & children]
  (js/console.log "Collapse render " is-collapsed)
  (into
   [:div.collapse {:class (when is-collapsed "active")}] children))

(defn tree-view-item [{:keys [label]} & children]
  (let [is-collapsed (atom false)]
    (fn [{:keys [label]} & children]
      [:li.tree-view-item 
       [:div
        {:on-click #(swap! is-collapsed not)}
        [:span.icon-container (when children "\u25B9")]
        [:span.label label]]
       (when children [collapse @is-collapsed
                       (into [:ul.tree-view-group] children)])])))

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