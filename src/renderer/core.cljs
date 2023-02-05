(ns renderer.core
  (:require [helix.core :refer [defnc $]]
            ["react-dom/client" :as rdom]))

(enable-console-print!)

(defnc app )

(defonce root (rdom/createRoot (js/document.getElementById "app-container")))
(defn ^:dev/after-load start! []
  (.render root ($ app)))