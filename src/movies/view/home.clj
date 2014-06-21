(ns movies.view.home
    (:require [compojure.core :refer [defroutes GET]]
              [stencil.core :as stencil]
              [movies.service.security :refer [authenticated?]]
              [movies.service.db :as db]
              [movies.view.common :refer [wrap-layout]]))

(defn- render-home [request]
  (stencil/render-file
   "movies/view/templates/home"
   {:movies (db/get-movies {} :page 1 :results 20)}))

(defn- render-index [request]
  (stencil/render-file
   "movies/view/templates/index"
   {}))

(defn- render-page [request]
  (wrap-layout "Home"
               (if (authenticated?)
                 (render-home request)
                 (render-index request))))

(defroutes home-routes
  (GET "/" request (render-page request)))
