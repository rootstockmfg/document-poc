# Simple Makefile for common dev tasks

MVN = mvn
LIQUIBASE_CMD = liquibase

NIX_SHELL = $(shell test -f flake.nix && echo flake || (test -f shell.nix && echo nix) || echo)

APP = document-poc
COMPOSE = docker-compose -f compose.yaml

ifeq ($(NIX_SHELL),flake)
  NIX_RUN = direnv exec . -- flake run --command
else ifeq ($(NIX_SHELL),nix)
  NIX_RUN = nix-shell --run
else
  NIX_RUN =
endif

-include .env

.PHONY: help build package start clean liquibase-update liquibase-rollback

help:
	@echo "Available targets:"
	@echo "  make build             - run 'mvn package'"
	@echo "  make package           - build jar skipping tests"
	@echo "  make start             - run the app with 'mvn spring-boot:run'"
	@echo "  make clean             - clean maven target"
	@echo "  make liquibase-update  - run liquibase update using nix-shell if available"
	@echo "  make liquibase-rollback - run liquibase rollbackCount 1 using nix-shell if available"

build:
	$(MVN) package

package:
	$(MVN) -DskipTests package

start:
	$(MVN) spring-boot:run

docker-compose:
	$(COMPOSE) up -d

docker-build:
	docker build . --tag $(APP):latest

docker-start: docker-build docker-compose
	docker run --rm -it --env-file .env -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:55559/mydatabase -e TESSDATA_PREFIX=/usr/share/tessdata -p $(PORT):$(PORT) --name $(APP) $(APP):latest

docker-stop:
	$(COMPOSE) stop $(APP)
	$(COMPOSE) rm -f $(APP)

docker-shell:
	$(COMPOSE) run -it $(APP) /bin/bash

docker-restart: docker-build
	$(COMPOSE) restart $(APP)

clean:
	$(MVN) clean

db-update:
	$(LIQUIBASE_CMD) update; \

db-rollback:
	@read -p "Rollback to tag/changeset/rollbackCount? (e.g. rollbackCount 1 or rollback <tag>) : " RB && \
	$(LIQUIBASE_CMD) $$RB;

db-reset:
	$(LIQUIBASE_CMD) drop-all
