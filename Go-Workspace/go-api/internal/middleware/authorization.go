package middleware

import (
	"errors"
	"net/http"

	"github.com/hjc0930/go-api/api"
	"github.com/hjc0930/go-api/internal/tools"
	"github.com/sirupsen/logrus"
)

var UnAuthorizationError = errors.New("Invalid username or token.")

func Authorization(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		var username string = r.URL.Query().Get("username")
		var token string = r.Header.Get("Authorization")
		var err error

		if username == "" || token == "" {
			logrus.Error(UnAuthorizationError)
			api.RequestErrorHandler(w, UnAuthorizationError)
			return
		}

		var database *tools.DatabaseInterface
		database, err = tools.NewDatabase()

		if err != nil {
			api.InternalErrorHandler(w)
			return
		}

		var loginDetails *tools.LoginDetails
		loginDetails = (*database).GetUserLoginDetails(username)

		if loginDetails == nil || (token != (*loginDetails).AuthToken) {
			logrus.Error(UnAuthorizationError)
			api.RequestErrorHandler(w, UnAuthorizationError)
			return
		}
		next.ServeHTTP(w, r)
	})
}
