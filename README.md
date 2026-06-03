# Rain

A small dynamically typed language — install it once, then run `rain hello.rain` anywhere (like Node.js).

## Install Rain (like Node.js)

### From source

Build a standalone `rain` binary (bundled Java — **your friends don't need JDK**):

```bash
git clone https://github.com/yugal109/rain.git
cd rain
./install.sh
```

Add to PATH if the installer tells you to:

```bash
export PATH="$HOME/.local/bin:$PATH"
```

Then use it like Node:

```bash
rain --version
rain examples/hello.rain
rain                    # REPL
```

### From a release

After you publish a [GitHub Release](https://github.com/yugal109/rain/releases):

```bash
curl -fsSL https://raw.githubusercontent.com/yugal109/rain/main/scripts/install-from-release.sh | sh
export PATH="$HOME/.local/bin:$PATH"
rain hello.rain
```

No Java install required — the release bundles a runtime.

### Uninstall

```bash
./install.sh --uninstall
```

## Features

- Variables, functions, closures, classes, inheritance (`super`)
- Control flow: `if` / `else`, `while`, `for`, `return`
- `print` (newline) and `printin` (no newline)
- Builtins: `clock`, `sqrt`, `sin`, `cos`, `rand`, `len`, `subStr`, and more — see `NativeFunctions.java`

## Hack on the language (developers)

Requires **JDK 17+** and **Make**:

```bash
make build
make run examples/hello.rain
make test
```

| Command | Description |
|---------|-------------|
| `make build` | Compile to `build/` |
| `make run [file.rain]` | Run script or REPL |
| `make package` | Standalone `dist/rain.app` (macOS) |
| `make release-asset` | `dist/rain-macos-arm64.tar.gz` for Releases |
| `make package-dmg` | macOS `.dmg` installer |
| `./install.sh --jar` | Install using system Java (smaller, needs JRE) |

## Example

```rain
fun greet(name) {
  print "Hello, " + name + "!";
}

greet("Rain");
```

More in [`examples/`](examples/).

## Publish a release (so friends can curl-install)

```bash
git tag v1.0.0
git push origin v1.0.0
```

GitHub Actions builds `rain-macos-arm64.tar.gz` and `rain-linux-x64.tar.gz` and attaches them to the release.

## Project layout

```
com/craftinginterpreters/     # Interpreter implementation (Java)
examples/                     # Sample .rain files
install.sh                    # Local install → ~/.local/bin/rain
scripts/install-from-release.sh
```

## Exit codes

| Code | Meaning |
|------|---------|
| 64 | Wrong CLI usage |
| 65 | Compile-time error |
| 70 | Runtime error |

## License

MIT — see [LICENSE](LICENSE).
