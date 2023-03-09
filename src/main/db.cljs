(ns main.db
  (:require ["sqlite3" :as sqlite3]
            [lambdaisland.glogi :as log]))


(defn create-connection [path]
  (let [sqlite3 (.verbose sqlite3)
        Database (.-Database sqlite3)]
    (-> (new Database path)
        (.on "trace" #(log/trace :query %))
        (.on "profile" #(log/trace :query %1 :execution-time %2)))))