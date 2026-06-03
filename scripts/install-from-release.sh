#!/usr/bin/env sh
# Download a pre-built Rain release from GitHub and install it.
# Usage: curl -fsSL https://raw.githubusercontent.com/yugal109/rain/main/scripts/install-from-release.sh | sh
set -e

REPO="yugal109/rain"
PREFIX="${PREFIX:-$HOME/.local}"
INSTALL_SHARE="$PREFIX/share/rain"
INSTALL_BIN="$PREFIX/bin"
VERSION="${RAIN_VERSION:-latest}"

os="$(uname -s | tr '[:upper:]' '[:lower:]')"
arch="$(uname -m)"
case "$arch" in
  arm64|aarch64) arch="arm64" ;;
  x86_64|amd64) arch="x64" ;;
  *) echo "Unsupported architecture: $arch" >&2; exit 1 ;;
esac

case "$os" in
  darwin) asset="rain-macos-${arch}.tar.gz" ;;
  linux) asset="rain-linux-${arch}.tar.gz" ;;
  *)
    echo "Unsupported OS: $os — install from source: git clone ... && ./install.sh --jar"
    exit 1
    ;;
esac

if [ "$VERSION" = "latest" ]; then
  url=$(curl -fsSL "https://api.github.com/repos/${REPO}/releases/${VERSION}" \
    | grep "browser_download_url.*${asset}\"" \
    | head -1 \
    | cut -d '"' -f 4)
else
  url="https://github.com/${REPO}/releases/download/v${VERSION}/${asset}"
fi

[ -n "$url" ] || {
  echo "No release found for ${asset}. Build from source:" >&2
  echo "  git clone https://github.com/${REPO}.git && cd rain && ./install.sh" >&2
  exit 1
}

tmpdir=$(mktemp -d)
trap 'rm -rf "$tmpdir"' EXIT

echo "Downloading $asset ..."
curl -fsSL "$url" | tar -xz -C "$tmpdir"

mkdir -p "$INSTALL_BIN" "$INSTALL_SHARE"
rm -rf "$INSTALL_SHARE/rain.app" "$INSTALL_SHARE/rain"

if [ -d "$tmpdir/rain.app" ]; then
  cp -R "$tmpdir/rain.app" "$INSTALL_SHARE/rain.app"
  RAIN_EXE="$INSTALL_SHARE/rain.app/Contents/MacOS/rain"
elif [ -d "$tmpdir/rain" ]; then
  cp -R "$tmpdir/rain" "$INSTALL_SHARE/rain"
  RAIN_EXE="$INSTALL_SHARE/rain/bin/rain"
else
  echo "install.sh: unexpected archive layout (expected rain.app or rain/)" >&2
  exit 1
fi

ln -sf "$RAIN_EXE" "$INSTALL_BIN/rain"

echo "Rain installed to $INSTALL_BIN/rain"
if ! echo ":$PATH:" | grep -q ":$INSTALL_BIN:"; then
  echo "Add to PATH: export PATH=\"$INSTALL_BIN:\$PATH\""
fi
