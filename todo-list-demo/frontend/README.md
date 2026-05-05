# Todo List - Frontend

React + TypeScript + Vite + MUI

## Tech Stack

- **Framework**: React 19 + TypeScript 6
- **Build Tool**: Vite 8
- **UI Library**: MUI (Material UI) 9
- **Styling**: Emotion
- **HTTP Client**: Axios
- **Routing**: React Router 7
- **Date Handling**: Day.js
- **Linting**: ESLint

## Getting Started

```bash
pnpm install          # Install dependencies
pnpm dev              # Start dev server
```

## Available Scripts

| Command | Description |
|---------|-------------|
| `pnpm dev` | Start dev server with HMR |
| `pnpm build` | TypeScript type check + production build |
| `pnpm preview` | Preview production build |
| `pnpm lint` | Run ESLint |

## pnpm Common Commands

### Dependency Management

```bash
pnpm add <pkg>               # Add production dependency
pnpm add -D <pkg>            # Add dev dependency
pnpm remove <pkg>            # Remove dependency
```

### Dependency Updates

```bash
pnpm outdated                 # Check outdated dependencies
pnpm update                   # Update within semver range
pnpm update -i                # Interactive update
pnpm update -i --latest       # Interactive update (including major versions)
```

### Inspect Dependencies

```bash
pnpm list                     # List dependencies
pnpm why <pkg>                # Show why a package is installed
pnpm info <pkg>               # View remote package info
```

### Cleanup & Reinstall

```bash
pnpm prune                    # Remove unused dependencies
pnpm store prune              # Prune global store cache
rm -rf node_modules && pnpm install  # Clean reinstall
```

### Production / CI

```bash
pnpm install --frozen-lockfile   # Strict lockfile install
pnpm install --prod              # Production dependencies only
pnpm prune --prod                # Remove devDependencies
```

## Expanding ESLint Configuration

If you are developing a production application, we recommend updating the configuration to enable type-aware lint rules:

```js
export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...

      // Remove tseslint.configs.recommended and replace with this
      tseslint.configs.recommendedTypeChecked,
      // Alternatively, use this for stricter rules
      tseslint.configs.strictTypeChecked,
      // Optionally, add this for stylistic rules
      tseslint.configs.stylisticTypeChecked,

      // Other configs...
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```

You can also install [eslint-plugin-react-x](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-x) and [eslint-plugin-react-dom](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-dom) for React-specific lint rules:

```js
// eslint.config.js
import reactX from 'eslint-plugin-react-x'
import reactDom from 'eslint-plugin-react-dom'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...
      // Enable lint rules for React
      reactX.configs['recommended-typescript'],
      // Enable lint rules for React DOM
      reactDom.configs.recommended,
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```
