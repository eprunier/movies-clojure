(ns movies.view.settings
  (:require [compojure.core :refer [defroutes GET]]
            [stencil.core :as stencil]
            [movies.service.security :refer [restricted authenticated?]]
            [movies.view.common :refer [wrap-layout]]))

(defn- page-body []
  (stencil/render-file
   "movies/view/templates/settings"
   {}))

(defn- render-page [request]
  (wrap-layout "Settings"
               (page-body)))

(defroutes settings-routes
  (GET "/settings" request (restricted authenticated? render-page request)))
