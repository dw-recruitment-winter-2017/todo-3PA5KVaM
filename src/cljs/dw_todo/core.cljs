(ns dw-todo.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

;; -------------------------
;; Data

(defonce data (atom {:todos []}))

(defn get-todos []
  (get-in @data [:todos]))

(defn add-todo [todo]
  (swap! data update-in [:todos] merge todo))

(defn add-todos [todos]
  (mapv add-todo todos))

;; -------------------------
;; Components

(defn todo-item [todo]
  [:div.todo-item
    [:input {:type "checkbox" :checked (:done todo)}]
    [:span (:text todo)]])

(defn todo-list []
  [:div#todo-list
    [:ul
      (for [todo (get-todos)]
        ^{:key (:id todo)}[todo-item todo])]])

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to dw-todo"]
   [:div [:a {:href "/about"} "go to about page"]]
   [todo-list]])

(defn about-page []
  [:div [:h2 "About dw-todo"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; Client

(defn client-get-todos []
  (go (let [response (<! (http/get "http://0.0.0.0:3449/api/todos"))]
        (add-todos (:body response)))))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (client-get-todos)
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
