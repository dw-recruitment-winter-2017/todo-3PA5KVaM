(ns dw-todo.api
  (:require [dw-todo.datastore :as data]))

;; middleware to generate and send EDN response
(defn generate-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})

(defn get-todos []
  (generate-response (data/get-todos)))

(defn add-todos [params]
  (let [result (data/add-todos (:todos params))]
    (generate-response result)))
