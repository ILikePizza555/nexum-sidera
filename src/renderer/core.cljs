(ns renderer.core
  (:require [helix.core :refer [defnc $]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            ["react-dom/client" :as rdom]
            [dv.cljs-emotion :refer [defstyled]]
            ["@react-spring/web" :refer [animated useSpring]]
            ["react-use-measure" :as use-measure]))

(enable-console-print!)

(defstyled tree-root :ul
  {:list-style-type "none"
   :margin 0
   :padding 0
   :user-select "none"})

(defstyled tree-content animated.ul
  {:list-style-type "none"
   :margin 0
   :padding-inline-start "1em"
   :overflow "hidden"})

(defstyled tree-header :div
  {:display "flex"
   :height "100%"
   :align-items "center"
   :cursor "pointer"})

(defstyled tree-icon :span
  {:width "1em"
   :height "1em"
   :padding-inline-end "0.5em"})

(defstyled tree-title :span
  {:vertical-align "middle"})

(defnc MinusSquare []
  (d/svg
   {:viewBox "64 -65 897 897"} 
   (d/g
    (d/path {:d "M888 760v0v0v-753v0h-752v0v753v0h752zM888 832h-752q-30 0 -51 -21t-21 -51v-753q0 -29 21 -50.5t51 -21.5h753q29 0 50.5 21.5t21.5 50.5v753q0 30 -21.5 51t-51.5 21v0zM732 347h-442q-14 0 -25 10.5t-11 25.5v0q0 15 11 25.5t25 10.5h442q14 0 25 -10.5t11 -25.5v0
  q0 -15 -11 -25.5t-25 -10.5z"}))))

(defnc PlusSquare []
  (d/svg
   {:viewBox "64 -65 897 897"}
   (d/g
    (d/path {:d "M888 760v0v0v-753v0h-752v0v753v0h752zM888 832h-752q-30 0 -51 -21t-21 -51v-753q0 -29 21 -50.5t51 -21.5h753q29 0 50.5 21.5t21.5 50.5v753q0 30 -21.5 51t-51.5 21v0zM732 420h-184v183q0 15 -10.5 25.5t-25.5 10.5v0q-14 0 -25 -10.5t-11 -25.5v-183h-184
  q-15 0 -25.5 -11t-10.5 -25v0q0 -15 10.5 -25.5t25.5 -10.5h184v-183q0 -15 11 -25.5t25 -10.5v0q15 0 25.5 10.5t10.5 25.5v183h184q15 0 25.5 10.5t10.5 25.5v0q0 14 -10.5 25t-25.5 11z"}))))

(defnc tree [{:keys [title children defaultOpen] :or {defaultOpen true}}]
  (let [[isOpen setOpen] (hooks/use-state defaultOpen)
        [ref bounds] (use-measure)
        viewHeight (.-height bounds)
        springFrom #js {:height 0 :opacity 0 :y 0}
        springTo #js {:height (if isOpen viewHeight 0)
                      :opacity (if isOpen 1 0)
                      :y (if isOpen 20 0)}
        styleValues (useSpring #js {:from springFrom :to springTo})
        icon (when children (if isOpen ($ MinusSquare) ($ PlusSquare)))] 
    (d/li
     (tree-header
      {:onClick #(setOpen (not isOpen))}
      (tree-icon icon)
      (tree-title title))
     (when children (tree-content {:style {:opacity (.-opacity styleValues)
                                           :height (.-height styleValues)}}
                     ($ animated.div {:ref ref :style {:y (.-y styleValues)}} children))))))

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