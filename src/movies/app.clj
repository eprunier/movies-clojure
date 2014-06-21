(ns movies.app
  (:require [clojure.core.cache :as cache]
            [compojure.core :refer [defroutes routes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [stencil.loader :as stencil]
            [movies.middleware.session :as session-manager]
            [movies.middleware.context :as context-manager]))

;;; Initialization
;; Add required code here (database, etc.)
(stencil/set-cache (cache/ttl-cache-factory {}))
;;(stencil/set-cache (cache/lru-cache-factory {}))


;;; Load public routes
(require '[movies.view.home :refer [home-routes]]
         '[movies.view.about :refer [about-routes]])

;;; Load registration and authentication routes
(require '[movies.view.auth :refer [auth-routes]])

;;; Load generic routes
(require '[movies.view.profile :refer [profile-routes]]
         '[movies.view.settings :refer [settings-routes]]
         '[movies.view.admin :refer [admin-routes]])

;;; Load website routes
(require '[movies.view.movie :refer [movie-routes]])


;; Ring handler definition
(defroutes site-handler
  (-> (routes home-routes
              about-routes
              auth-routes
              profile-routes
              settings-routes
              admin-routes
              movie-routes
              (route/resources "/")
              (route/not-found "<h1>Page not found.</h1>"))
      (session-manager/wrap-session)
      (context-manager/wrap-context-root)
      (handler/site)))
