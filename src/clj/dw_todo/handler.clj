(ns dw-todo.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [dw-todo.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]))

;; DATA

(defonce data (atom {
  :todos [
    {:id 0 :done (boolean false) :text "Learn Clojure"}
    {:id 1 :done (boolean false) :text "Build a Todo App"}]}))

(defn get-todos []
  (get-in @data [:todos]))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")]))

;; middleware to generate and send EDN response
(defn generate-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body (pr-str data)})

;; API
(defn api-get-todos []
  (generate-response (get-todos)))

(defroutes routes
  (GET "/" [] (loading-page))
  (GET "/about" [] (loading-page))
  (GET "/api/todos" [] (api-get-todos))
  
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
