#!/usr/bin/env sh
# Install Rain so you can run: rain hello.rain
# No need to clone the repo or run make after install.
set -e

PREFIX="${PREFIX:-$HOME/.local}"
INSTALL_SHARE="$PREFIX/share/rain"
INSTALL_BIN="$PREFIX/bin"
REPO_ROOT="$(cd "$(dirname "$0")" && pwd)"

usage() {
  cat <<'EOF'
Usage: ./install.sh [options]

Install the Rain interpreter to your machine (like installing Node.js).

Options:
  --prefix DIR   Install location (default: ~/.local)
  --jar          Install JAR + launcher only (requires Java on PATH)
  --binary       Install bundled runtime (default on macOS if available)
  --uninstall    Remove Rain from PREFIX
  -h, --help     Show this help

After install, ensure PREFIX/bin is on your PATH:
  export PATH="$HOME/.local/bin:$PATH"

Then run:
  rain examples/hello.rain
  rain
EOF
}

die() {
  echo "install.sh: $*" >&2
  exit 1
}

ensure_prefix_bin() {
  mkdir -p "$INSTALL_BIN" "$INSTALL_SHARE"
}

install_jar() {
  command -v java >/dev/null 2>&1 || die "Java not found. Install JDK 17+ or use ./install.sh --binary"

  if [ ! -f "$REPO_ROOT/dist/rain.jar" ]; then
    echo "Building rain.jar..."
    (cd "$REPO_ROOT" && make jar)
  fi

  ensure_prefix_bin
  cp "$REPO_ROOT/dist/rain.jar" "$INSTALL_SHARE/rain.jar"

  cat >"$INSTALL_BIN/rain" <<EOF
#!/usr/bin/env sh
exec java -jar "$INSTALL_SHARE/rain.jar" "\$@"
EOF
  chmod +x "$INSTALL_BIN/rain"
}

install_binary() {
  MAC_APP="$REPO_ROOT/dist/rain.app"
  LINUX_APP="$REPO_ROOT/dist/rain"

  if [ ! -d "$MAC_APP" ] && [ ! -d "$LINUX_APP" ]; then
    echo "Building standalone Rain (bundled Java, no JDK needed to run)..."
    (cd "$REPO_ROOT" && make package)
  fi

  ensure_prefix_bin
  rm -rf "$INSTALL_SHARE/rain.app" "$INSTALL_SHARE/rain"

  if [ -d "$MAC_APP" ]; then
    cp -R "$MAC_APP" "$INSTALL_SHARE/rain.app"
    ln -sf "$INSTALL_SHARE/rain.app/Contents/MacOS/rain" "$INSTALL_BIN/rain"
  elif [ -d "$LINUX_APP" ]; then
    cp -R "$LINUX_APP" "$INSTALL_SHARE/rain"
    ln -sf "$INSTALL_SHARE/rain/bin/rain" "$INSTALL_BIN/rain"
  else
    die "Missing dist/rain.app or dist/rain — run 'make package' first"
  fi
}

uninstall() {
  rm -f "$INSTALL_BIN/rain"
  rm -rf "$INSTALL_SHARE"
  echo "Removed Rain from $PREFIX"
}

MODE=""

while [ $# -gt 0 ]; do
  case "$1" in
    --prefix)
      PREFIX="$2"
      INSTALL_SHARE="$PREFIX/share/rain"
      INSTALL_BIN="$PREFIX/bin"
      shift 2
      ;;
    --jar) MODE=jar; shift ;;
    --binary) MODE=binary; shift ;;
    --uninstall) uninstall; exit 0 ;;
    -h|--help) usage; exit 0 ;;
    *) die "Unknown option: $1" ;;
  esac
done

if [ -z "$MODE" ]; then
  case "$(uname -s)" in
    Darwin) MODE=binary ;;
    *) MODE=jar ;;
  esac
fi

case "$MODE" in
  jar) install_jar ;;
  binary) install_binary ;;
esac

echo ""
echo "Rain installed."
echo "  binary: $INSTALL_BIN/rain"
echo "  data:   $INSTALL_SHARE"
echo ""
if ! echo ":$PATH:" | grep -q ":$INSTALL_BIN:"; then
  echo "Add to your shell profile (~/.zshrc or ~/.bashrc):"
  echo "  export PATH=\"$INSTALL_BIN:\$PATH\""
  echo ""
fi
echo "Try: rain --version  (after opening a new terminal, or run the export above)"
echo "     rain $REPO_ROOT/examples/hello.rain"
