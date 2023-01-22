(ns main.core
    (:require ["electron" :refer [app BrowserWindow]]))

(defn create-window []
    (let [window (BrowserWindow. (clj->js {:width 800
                                           :height 600}))]
        (.loadURL window (str "file://" js/__dirname "/public/index.html"))
        (.openDevTools (.-webContents window))))

(defn main []
    (.on app "window-all-closed" #(when-not (= js/process.platform "darwin")
                                        (.quit app)))
    (.on app "ready" create-window))