(ns movies.view.movie
    (:require [compojure.core :refer [defroutes GET]]
              [stencil.core :as stencil]
              [movies.service.security :refer [restricted authenticated?]]
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

(defn- movie [request]
  (wrap-layout "Movie"
               (stencil/render-file
                "movies/view/templates/movie"
                (db/get-movie (get-in request [:params :id])))))

(defroutes movie-routes
  (GET "/movie/:id" request (restricted authenticated? movie request)))
