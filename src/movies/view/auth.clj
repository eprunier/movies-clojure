(ns movies.view.auth
    (:require [ring.util.response :as response]
              [compojure.core :refer [defroutes GET POST]]
              [stencil.core :as stencil]
              [movies.middleware.context :refer [url]]
              [movies.middleware.session :as session]
              [movies.service.security :refer [authenticated?]]
              [movies.service.db :as db]
              [movies.view.common :refer [wrap-layout]]))

(defn- signup-page
  "Render the signup page."
  [request]
  (wrap-layout
   "Sign up"
   (stencil/render-file
    "movies/view/templates/signup"
    {})))

(defn- signup
  "Process account creation.
   if success : retuns 'ok'
   if error : returns a message to be displayed to the user"
  [request]
  ;; TODO : process sign up and return "ok" if success
  (let [params (:params request)
        username (:username params)
        user {:username username
              :email (:email params)
              :type :user}]
    (if (clojure.string/blank? username)
      "The username can't be blank."
      (if (db/add-user user)
        "ok"
        (str "The username " username " is already used.")))))

(defn- login-page
  "Render the login page."
  [request]
  (wrap-layout
   "Log in"
   (stencil/render-file
    "movies/view/templates/login"
    {})))

(defn- auth
  "Initialise session with dummy data."
  [request]
  ;; TODO : replace with the authentication process
  (let [user (db/get-user (-> request
                              :params
                              :username))]
    (when user
      (session/set-user! (select-keys user [:username :type]))
      user)))

(defn- login
  "Process user login with username/password.
   if success : retuns 'ok'
   if error : returns a message to be displayed to the user"
  [request]
  (if (auth request)
    "ok"
    "Authentication failed. Please check your username and password."))

(defn- reset-pass-page
  "Render the reset password page."
  [request]
  (wrap-layout
   "Reset password"
   (stencil/render-file
    "movies/view/templates/reset-pass"
    {})))

(defn- reset-pass
  "Reset the user password.
   if success : retuns 'ok'
   if error : returns a message to be displayed to the user"
  [request]
  "ok")

(defn- check-session
  "Check session and returns 'valid' if it is."
  [request]
  (when (authenticated?)
    "valid"))

(defn- logout
  "Process user logout."
  [request]
  (session/clear)
  (response/redirect (url "/")))

(defroutes auth-routes
  (GET "/signup" request (signup-page request))
  (POST "/signup" request (signup request))
  (GET "/login" request (login-page request))
  (POST "/login" request (login request))
  (GET "/reset-pass" request (reset-pass-page request))
  (POST "/reset-pass" request (reset-pass request))
  (GET "/check-session" request (check-session request))
  (GET "/logout" request (logout request)))
