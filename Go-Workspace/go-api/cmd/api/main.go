package main

import (
	"fmt"
	"net/http"

	"github.com/go-chi/chi"
	"github.com/hjc0930/go-api/internal/handlers"
	log "github.com/sirupsen/logrus"
)

func main() {
	log.SetReportCaller(true)
	// Initialize the router
	var r *chi.Mux = chi.NewRouter()
	handlers.Handler(r)

	fmt.Println("Starting Go API services...")

	err := http.ListenAndServe("localhost:8080", r)

	if err != nil {
		log.Error(err)
	}
}
