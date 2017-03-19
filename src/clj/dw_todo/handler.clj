(ns dw-todo.handler
  (:require [compojure.core :refer [GET PUT POST DELETE defroutes]]
            [compojure.route :refer [not-found resources]]
            [hiccup.page :refer [include-js include-css html5]]
            [dw-todo.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]
            [dw-todo.api :as api]))

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

(defroutes routes
  (GET  "/"               [] (loading-page))
  (GET  "/about"          [] (loading-page))
  (GET  "/api/todos"      [] (api/get-todos))
  (POST "/api/todos"      request (api/add-todos (:edn-params request)))
  (PUT  "/api/todos/:id"  request (api/update-todo (:edn-params request)))
  
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
