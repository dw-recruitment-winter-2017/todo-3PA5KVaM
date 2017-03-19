(ns dw-todo.middleware
  (:require [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.edn :refer [wrap-edn-params]]))

(defn wrap-middleware [handler]
  (-> handler
      ;; (wrap-defaults site-defaults)
      ;; Had to hack the defaults to disable CSRF protection, which works fine for local dev
      ;; But would actually need to enable CSRF in production
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      wrap-exceptions
      wrap-reload
      wrap-edn-params))
