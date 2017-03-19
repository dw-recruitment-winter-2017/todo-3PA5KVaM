(ns dw-todo.client
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [dw-todo.datastore :as data]))

(defonce apiUrl "http://0.0.0.0:3449/api/todos")

(defn get-todos []
  (go (let [response (<! (http/get apiUrl))]
        (data/add-todos (:body response)))))

(defn add-todos [todos]
  (go (let [response (<! (http/post apiUrl {:edn-params {:todos todos}}))]
        (data/add-todos (:body response)))))

(defn add-todo-from-string [todo-string]
  (add-todos (conj [] {:status 0 :text todo-string})))

(defn update-todo [todo]
  (go (let [response (<! (http/put (str apiUrl "/" (:id todo)) {:edn-params todo}))]
        (if (not= (:body response) nil)
          (data/update-todo (:body response))))))

(defn remove-todo [todo]
  (go (let [todo (assoc-in todo [:status] 2)
            response (<! (http/put (str apiUrl "/" (:id todo)) {:edn-params todo}))]
        (if (not= (:body response) nil)
          (data/update-todo (:body response))))))
