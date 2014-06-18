(ns movies.view.profile
  (:require [compojure.core :refer [defroutes GET]]
            [stencil.core :as stencil]
            [movies.service.security :refer [restricted authenticated?]]
            [movies.view.common :refer [wrap-layout]]))

(defn- page-body []
  (stencil/render-file
   "movies/view/templates/profile"
   {}))

(defn- render-page [request]
  (wrap-layout "Profile"
               (page-body)))

(defroutes profile-routes
  (GET "/profile" request (restricted authenticated? render-page request)))
