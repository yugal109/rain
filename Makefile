SRC = com/craftinginterpreters/lox
SOURCES = $(wildcard $(SRC)/*.java)
OUT_DIR = build
DIST_DIR = dist
JPACKAGE_INPUT = $(DIST_DIR)/jpackage-input
MAIN_CLASS = com.craftinginterpreters.lox.Lox
JAR = $(DIST_DIR)/rain.jar
MANIFEST = $(DIST_DIR)/MANIFEST.MF
APP_NAME = rain
APP_VERSION = 1.0.0
UNAME_S := $(shell uname -s)
UNAME_M := $(shell uname -m)

.PHONY: build run jar clean test install package package-mac package-dmg release-asset uninstall

build:
	mkdir -p $(OUT_DIR)
	javac -d $(OUT_DIR) $(SOURCES)

run: build
	java -cp $(OUT_DIR) $(MAIN_CLASS) $(filter-out $@,$(MAKECMDGOALS))

jar: build
	mkdir -p $(DIST_DIR)
	printf 'Manifest-Version: 1.0\nMain-Class: $(MAIN_CLASS)\n' > $(MANIFEST)
	jar cfm $(JAR) $(MANIFEST) -C $(OUT_DIR) .

test: build
	java -cp $(OUT_DIR) $(MAIN_CLASS) examples/hello.rain
	java -cp $(OUT_DIR) $(MAIN_CLASS) examples/stars.rain

# Standalone app with bundled JRE — friends don't need Java installed
# macOS → dist/rain.app   Linux → dist/rain/ (with bin/rain)
package: jar
	rm -rf $(JPACKAGE_INPUT) $(DIST_DIR)/$(APP_NAME).app $(DIST_DIR)/$(APP_NAME)
	mkdir -p $(JPACKAGE_INPUT)
	cp $(JAR) $(JPACKAGE_INPUT)/
	jpackage \
		--input $(JPACKAGE_INPUT) \
		--name $(APP_NAME) \
		--main-jar rain.jar \
		--main-class $(MAIN_CLASS) \
		--app-version $(APP_VERSION) \
		--dest $(DIST_DIR) \
		--type app-image

package-mac: package

package-dmg: package
	jpackage \
		--input $(JPACKAGE_INPUT) \
		--name $(APP_NAME) \
		--main-jar rain.jar \
		--main-class $(MAIN_CLASS) \
		--app-version $(APP_VERSION) \
		--dest $(DIST_DIR) \
		--type dmg

# Tarball for GitHub Releases (rain-macos-arm64.tar.gz)
release-asset: package
	@arch="$(UNAME_M)"; \
	case "$$arch" in \
	  arm64|aarch64) asset_arch=arm64 ;; \
	  x86_64) asset_arch=x64 ;; \
	  *) echo "Unsupported arch: $$arch"; exit 1 ;; \
	esac; \
	case "$(UNAME_S)" in \
	  Darwin) asset_os=macos ;; \
	  Linux) asset_os=linux ;; \
	  *) echo "Unsupported OS: $(UNAME_S)"; exit 1 ;; \
	esac; \
	tarball="$(DIST_DIR)/rain-$$asset_os-$$asset_arch.tar.gz"; \
	case "$(UNAME_S)" in \
	  Darwin) image="$(APP_NAME).app" ;; \
	  Linux) image="$(APP_NAME)" ;; \
	esac; \
	COPYFILE_DISABLE=1 tar -czf "$$tarball" -C "$(DIST_DIR)" "$$image"; \
	echo "Created $$tarball"

install:
	chmod +x install.sh scripts/rain scripts/install-from-release.sh
	./install.sh

uninstall:
	./install.sh --uninstall

clean:
	rm -rf $(OUT_DIR) $(DIST_DIR)

%:
	@:
