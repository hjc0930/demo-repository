.PHONY: run build test test-e2e docker-build docker-up docker-down

run:
	npx nest start --watch

build:
	npx nest build

test:
	npx jest --forceExit

test-e2e:
	npx jest --config ./test/jest-e2e.json --forceExit

docker-build:
	docker compose build

docker-up:
	docker compose up -d

docker-down:
	docker compose down
