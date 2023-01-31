(ns renderer.core
  (:require [reagent.core :refer [atom as-element]]
            [reagent.dom :as rd]
            ["react-dom/client" :refer [createRoot]]))

(enable-console-print!)

(defonce root (createRoot (js/document.getElementById "app-container")))
(defonce state (atom 0))

(defn root-component []
  [:div
   [:div.logos
    [:img.electron {:src "img/electron-logo.png"}]
    [:img.cljs {:src "img/cljs-logo.svg"}]
    [:img.reagent {:src "img/reagent-logo.png"}]]
   [:button
    {:on-click #(swap! state inc)}
    (str "Clicked " @state " times")]])

(defn ^:dev/after-load start! []
  (.render root (as-element [root-component])))