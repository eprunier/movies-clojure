(ns movies.service.db
  (:require [monger.core :refer [connect get-db]]
            [monger.query :refer [with-collection paginate] :as query]
            [monger.collection :as monger])
  (:import [org.bson.types ObjectId]))

(defonce connection (connect))
(defonce db (get-db connection "movies"))

;;
;; User management
;; 

(defn get-user
  "Returns the user corresponding to the given username."
  [username]
  (monger/find-one-as-map db "user" {:username username}))

(defn add-user
  "Add a new user to database."
  [{:keys [username] :as user}]
  (when (and username
             (not (get-user username)))
    (monger/insert db "user" user)))


;; 
;; Movie management
;;

(defn get-movies
  "Find movies with or without criteria."
  [criteria & {:keys [page results] :as options}]
  (with-collection db "movie"
    (query/find criteria)
    (paginate :page page :per-page results)))

(defn get-movie
  [id]
  (monger/find-map-by-id db "movie" (ObjectId. id)))

(defn add-movie
  "Add movie."
  [movie]
  (monger/insert db "movie" movie))
