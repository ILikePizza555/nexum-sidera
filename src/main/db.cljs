(ns main.db
  (:require ["sqlite3" :as sqlite3]
            [lambdaisland.glogi :as log]
            [cljs.core.async :refer [chan put! close!]]))

(defn create-connection [path]
  (let [sqlite3 (.verbose sqlite3)
        Database (.-Database sqlite3)]
    (-> (new Database path)
        (.on "trace" #(log/trace :query %))
        (.on "profile" #(log/trace :query %1 :execution-time %2)))))

(defn ^:private run-cb [out]
  (fn [err] 
    (if err 
      (put! out {:error err})
      (this-as ^object this
               (put! out {:last-id (.lastID this) 
                          :changes (.changes this)})))
    (close! out)))

(defn run
  "CLJS Wrapper around the [sqlite3/Database.run()](https://github.com/TryGhost/node-sqlite3/wiki/API#runsql--param---callback) method.
   
   `db` - The `sqlite3/Database` object.

   `sql` - The sql query string to execute

   `params` - The params to bind to the query. This is automatically convered to a JS object.

   Returns a channel which will be used to output the results of the callback.
   "
  ([db sql params]
   (let [out chan]
     (.run db sql (clj->js params) (run-cb out))
     out)))

(defn prepare
  "CLJS Wrapper around the [sqlite3/Database.prepare()](https://github.com/TryGhost/node-sqlite3/wiki/API#preparesql--param---callback) method.
   
   `db` - The `sqlite3/Database` object.
   
   `sql` - The sql query to make into a prepared statement.
   
   `params` - (Optional) The parameters to bind to the statement. This is automatically converted to a JS object.
   
   Returns a channel which wil be used to output the results of the callback. On success, the channel will output `:success` and close."
  ([db sql] (prepare db sql []))
  ([db sql params]
   (let [out chan]
     (.prepare db sql (clj->js params) (fn [err] 
                                         (if err (put! out {:error err}) (put! out :success))
                                         (close! out)))
     out)))

(defn statement-run
  ([stmt] (statement-run stmt []))
  ([stmt params]
   (let [out chan] 
     (.run stmt (clj->js params) (run-cb out))
     out)))