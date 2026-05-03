.PHONY: run build swagger test clean

APP_NAME=todo-server
BUILD_DIR=bin

run:
	go run ./cmd/server

build:
	CGO_ENABLED=0 go build -o $(BUILD_DIR)/$(APP_NAME) ./cmd/server

swagger:
	swag init -g cmd/server/main.go -o docs

test:
	go test ./... -v

clean:
	rm -rf $(BUILD_DIR)
