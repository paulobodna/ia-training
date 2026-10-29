.PHONY: local-up local-down local-logs reset-db up down logs build rebuild restart clean status checkstyle spotbugs verify

local-up:
	docker compose -f docker-compose.local.yml up -d

local-down:
	docker compose -f docker-compose.local.yml down

local-logs:
	docker compose -f docker-compose.local.yml logs -f

reset-db:
	docker compose -f docker-compose.local.yml down -v

up:
	docker compose up -d

down:
	docker compose down

logs:
	docker compose logs -f

build:
	docker compose build

rebuild:
	docker compose up -d --build

restart:
	docker compose down
	docker compose up -d --build

clean:
	docker compose down -v

status:
	docker compose -f docker-compose.local.yml ps

spotbugs:
	cd backend/ && ./mvnw compile spotbugs:check

checkstyle:
	cd backend/ && ./mvnw checkstyle:check

verify:
	cd backend/ && ./mvnw verify