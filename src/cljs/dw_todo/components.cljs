(ns dw-todo.components
  (:require [reagent.core :refer [atom]]
            [dw-todo.datastore :as data]
            [dw-todo.client :as client]))

;; local data handling

(def input-text (atom ""))

(defn reset-input-text []
  (reset! input-text ""))

(defn submit-input []
  (if (not= @input-text "")
    (do
      (client/add-todo-from-string @input-text)
      (reset-input-text))))

;; TODO Components

(defn todo-input []
  [:input {:type "text"
           :placeholder "Do something"
           :value @input-text
           :on-change #(reset! input-text (-> % .-target .-value))}])

(defn todo-form []
  [:div.todo-form
    [todo-input]
    [:button {:on-click #(submit-input)} "Add it!"]])

(defn todo-item [todo]
  [:div.todo-item
    [:input {:type "checkbox" :checked (:done todo)}]
    [:span (:text todo)]])

(defn todo-list []
  [:div#todo-list
    (for [todo (data/get-todos)]
      ^{:key (:id todo)}[todo-item todo])])

;; Pages

(defn home-page []
  [:div
    [:h2 "Do Stuff! "
      [:span.title-link "(" [:a {:href "/about"} "huh?"] ")"]]
   [todo-list]
   [todo-form]])

(defn about-page []
  [:div [:h2 "About This App"]
    [:div 
      [:p "This is an app for keeping track of things that need to get done. What are you doing here? "
        [:a {:href "/"} "Do all the things"] "!"]]])
