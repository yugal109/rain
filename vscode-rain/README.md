# Rain for VS Code

Syntax highlighting for `.rain` files.

## Install locally

1. Open this folder in VS Code: `rain/vscode-rain`
2. Command Palette → **Developer: Install Extension from Location…**
3. Select this folder (`vscode-rain`)
4. Reload the window if prompted
5. Open any `.rain` file — bottom-right should say **Rain**

## Try without installing

1. Open `vscode-rain` in VS Code
2. Press **F5** — opens a new window with the extension loaded
3. Open `examples/hello.rain` in that window

## Publish later (optional)

```bash
npm install -g @vscode/vsce
vsce package
```

Then install the `.vsix` from the Extensions view, or publish to the Marketplace.
