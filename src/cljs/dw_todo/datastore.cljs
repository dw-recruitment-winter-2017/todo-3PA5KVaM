(ns dw-todo.datastore
  (:require [reagent.core :refer [atom]]))

(defonce data (atom {:todos []}))

(defn get-todos []
  (get-in @data [:todos]))

(defn add-todo [todo]
  (swap! data update-in [:todos] merge todo))

(defn add-todos [todos]
  (mapv add-todo todos))
