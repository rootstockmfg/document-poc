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

clean:
	$(MVN) clean

liquibase-update:
	@if [ -n "$(NIX_SHELL)" ]; then \
	  echo "Running Liquibase inside nix environment..."; \
	  $(NIX_RUN) "$(LIQUIBASE_CMD) update"; \
	else \
	  echo "Running Liquibase from PATH..."; \
	  $(LIQUIBASE_CMD) update; \
	fi

liquibase-rollback:
	@read -p "Rollback to tag/changeset/rollbackCount? (e.g. rollbackCount 1 or rollback <tag>) : " RB && \
	if [ -n "$(NIX_SHELL)" ]; then \
	  echo "Running Liquibase rollback inside nix environment..."; \
	  $(NIX_RUN) "$(LIQUIBASE_CMD) $$RB"; \
	else \
	  echo "Running Liquibase rollback from PATH..."; \
	  $(LIQUIBASE_CMD) $$RB; \
	fi

# Maven-backed Liquibase targets (non-interactive)
.PHONY: liquibase-mvn-update liquibase-mvn-rollback

# Usage: make liquibase-mvn-update LIQUIBASE_URL=jdbc:... LIQUIBASE_USER=user LIQUIBASE_PASS=pass
liquibase-mvn-update:
	@echo "Running Liquibase via Maven plugin..."
	$(MVN) liquibase:update \
		-Dliquibase.url="$(LIQUIBASE_URL)" \
		-Dliquibase.username="$(LIQUIBASE_USER)" \
		-Dliquibase.password="$(LIQUIBASE_PASS)"

# Usage: make liquibase-mvn-rollback COUNT=1 LIQUIBASE_URL=... LIQUIBASE_USER=... LIQUIBASE_PASS=...
liquibase-mvn-rollback:
	@if [ -n "$(COUNT)" ]; then \
	  ROLL_ARG=-Dliquibase.rollbackCount=$(COUNT); \
	else \
	  echo "Please pass COUNT=... to specify rollbackCount (e.g. make liquibase-mvn-rollback COUNT=1)"; exit 1; \
	fi; \
	$(MVN) liquibase:rollback $$ROLL_ARG \
		-Dliquibase.url="$(LIQUIBASE_URL)" \
		-Dliquibase.username="$(LIQUIBASE_USER)" \
		-Dliquibase.password="$(LIQUIBASE_PASS)"
