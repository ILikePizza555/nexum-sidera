(ns main.db
  (:require ["sqlite3" :as sqlite3]
            [lambdaisland.glogi :as log]
            [cljs.core.async :refer [put!]]))

(defn create-connection [path]
  (let [sqlite3 (.verbose sqlite3)
        Database (.-Database sqlite3)]
    (-> (new Database path)
        (.on "trace" #(log/trace :query %))
        (.on "profile" #(log/trace :query %1 :execution-time %2)))))

(defn ^:private run-cb [out]
  (fn [err] (if err
              (put! out {:error err})
              (this-as this
                       (put! out {:last-id (.lastID this)
                                  :changes (.changes this)})))))

(defn run
  "CLJS Wrapper around the [sqlite3/Database.run()](https://github.com/TryGhost/node-sqlite3/wiki/API#runsql--param---callback) method.
   Calls `run()` on the provided function with the provided `sql` query string, cljs->js converted `params`, and a callback function which puts
   the result of the query -- or any errors -- on the `out` channel."
  ([db sql params] (run db sql params nil))
  ([db sql params out]
   (.run db sql (clj->js params)
         (when out (run-cb out)))))

(defn prepare
  ([db sql] (prepare db sql [] nil))
  ([db sql params] (prepare db sql params nil))
  ([db sql params out]
   (.prepare db sql (clj->js params)
             (when out (fn [err] (if err
                                   (put! out {:error err})
                                   (put! out :success)))))))

(defn statement-run
  ([stmt] (statement-run stmt [] nil))
  ([stmt params] (statement-run stmt params nil))
  ([stmt params out]
   (.run stmt (clj->js params)
         (when out (run-cb out)))))