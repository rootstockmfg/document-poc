# Simple Makefile for common dev tasks

MVN = mvn
LIQUIBASE_CMD = liquibase

NIX_SHELL = $(shell test -f flake.nix && echo flake || (test -f shell.nix && echo nix) || echo)

ifeq ($(NIX_SHELL),flake)
  NIX_RUN = direnv exec . -- flake run --command
else ifeq ($(NIX_SHELL),nix)
  NIX_RUN = nix-shell --run
else
  NIX_RUN =
endif

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
	docker-compose -f compose.yaml up -d

docker-build:
	docker-compose -f compose.yaml build

docker-start: docker-build
	docker-compose -f compose.yaml up document-poc

docker-shell:
	docker-compose -f compose.yaml run -it document-poc /bin/bash

clean:
	$(MVN) clean

db-update:
	$(LIQUIBASE_CMD) update; \

db-rollback:
	@read -p "Rollback to tag/changeset/rollbackCount? (e.g. rollbackCount 1 or rollback <tag>) : " RB && \
	$(LIQUIBASE_CMD) $$RB;

db-reset:
	$(LIQUIBASE_CMD) drop-all
