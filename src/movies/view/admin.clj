(ns movies.view.admin
  (:require [compojure.core :refer [defroutes GET]]
            [stencil.core :as stencil]
            [movies.service.security :refer [restricted admin?]]
            [movies.view.common :refer [wrap-layout]]))

(defn- page-body []
  (stencil/render-file
   "movies/view/templates/admin"
   {}))

(defn- render-page [request]
  (wrap-layout "Admin"
               (page-body)))

(defroutes admin-routes
  (GET "/admin" request (restricted admin? render-page request)))
