package handlers

import (
	"github.com/go-chi/chi"
	chimiddle "github.com/go-chi/chi/middleware"
	"github.com/hjc0930/go-api/internal/middleware"
)

func Handler(r *chi.Mux) {
	r.Use(chimiddle.StripSlashes)
	r.Route("/account", func(router chi.Router) {
		// Middleware for /account route
		router.Use(middleware.Authorization)
		router.Get("/coins", GetCoinBalance)
	})
}
