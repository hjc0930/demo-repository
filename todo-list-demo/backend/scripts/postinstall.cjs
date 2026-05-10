const { execSync } = require('child_process');
const { existsSync } = require('fs');
const path = require('path');

// Find the better-sqlite3 directory in pnpm's virtual store
const pnpmDir = path.join(__dirname, '..', 'node_modules', '.pnpm');
if (!existsSync(pnpmDir)) {
  console.log('[postinstall] .pnpm directory not found, skipping better-sqlite3 rebuild');
  process.exit(0);
}

const { readdirSync } = require('fs');
const entries = readdirSync(pnpmDir);
const sqliteDir = entries.find((e) => e.startsWith('better-sqlite3@'));

if (!sqliteDir) {
  console.log('[postinstall] better-sqlite3 not found, skipping rebuild');
  process.exit(0);
}

const packageDir = path.join(pnpmDir, sqliteDir, 'node_modules', 'better-sqlite3');
const bindingFile = path.join(packageDir, 'build', 'Release', 'better_sqlite3.node');

if (existsSync(bindingFile)) {
  console.log('[postinstall] better-sqlite3 already built, skipping');
  process.exit(0);
}

console.log('[postinstall] Building better-sqlite3 native module...');
try {
  execSync(`npx --yes node-gyp rebuild --directory="${packageDir}"`, {
    stdio: 'inherit',
    cwd: packageDir,
  });
  console.log('[postinstall] better-sqlite3 built successfully');
} catch (err) {
  console.error('[postinstall] Failed to build better-sqlite3:', err.message);
  console.log('[postinstall] Try running: npx node-gyp rebuild --directory=node_modules/.pnpm/better-sqlite3@*/node_modules/better-sqlite3');
  process.exit(0); // Don't fail the install
}
