(ns dw-todo.components
  (:require [reagent.core :refer [atom]]
            [dw-todo.datastore :as data]
            [dw-todo.client :as client]))

(def input-text (atom ""))

;; helper functions

(defn reset-input-text []
  (reset! input-text ""))

(defn submit-input []
  (if (not= @input-text "")
    (do
      (client/add-todo-from-string @input-text)
      (reset-input-text))))

(defn toggle-todo [todo]
  (client/update-todo (assoc-in todo [:done] (not (:done todo)))))

;; TODO Components

(defn todo-checkbox [todo]
  [:input {:type "checkbox"
           :checked (:done todo)
           :on-change #(toggle-todo todo)}])

(defn todo-input []
  [:input {:type "text"
           :placeholder "Do something"
           :value @input-text
           :on-change #(reset! input-text (-> % .-target .-value))
           :on-key-down #(if (= (.-which %) 13) (submit-input))}])

(defn todo-form []
  [:div.todo-form
    [todo-input]
    [:button {:on-click #(submit-input)} "Add it!"]])

(defn todo-item [todo]
  [:div.todo-item {:class (if (= (:done todo) true) "done")}
    [todo-checkbox todo]
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
