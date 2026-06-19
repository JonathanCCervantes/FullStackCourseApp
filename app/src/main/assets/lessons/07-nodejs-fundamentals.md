# Module 07: Node.js Fundamentals

## Learning Objectives

By the end of this module you will be able to:
- Explain what Node.js is and why it lets JavaScript run on a server.
- Initialize a Node project with `npm init` and understand `package.json`.
- Install and use packages with `npm install`.
- Use Node's built-in modules.
- Write and run a script with Node directly from the terminal.

## Estimated Time
2–3 hours.

## A Note on Module Style

There are two ways to write `import`/`export` in Node: the older **CommonJS** style (`require()` / `module.exports`) and the newer **ES Modules** style (`import` / `export`). This course uses **CommonJS** throughout the backend, because it requires zero extra configuration and is still the most common style in tutorials, existing packages, and real-world Express projects. Just be aware ES Modules exist, in case you see `import` syntax elsewhere later.

---

## Concepts

### What Is Node.js, Really?

JavaScript was originally built to run only inside browsers. Node.js is a runtime that takes the same JavaScript language and lets it run directly on a computer/server, with access to things browsers intentionally block for security (reading/writing files, opening network ports, talking to databases). This is why the exact language you learned in Module 05 is also what powers your backend.

### `package.json` — Your Project's Identity Card

Every Node project has a `package.json` file listing: the project's name, version, the packages (dependencies) it needs, and custom shortcut commands (`scripts`).

### npm (Node Package Manager)

The JavaScript ecosystem has an enormous library of free, reusable, open-source packages (e.g., Express, Mongoose, bcrypt — all used later in this course) hosted on a registry called npm. `npm install <package-name>` downloads a package into a `node_modules` folder and records it in `package.json`.

### Node's Built-in Modules

Node ships with modules you can use without installing anything, via `require("module-name")`:

```javascript
const fs = require("fs");     // file system - read/write files
const path = require("path"); // build file paths safely across OSes
```

### Reading/Writing Files

```javascript
const fs = require("fs");

// Write a file (creates it if it doesn't exist, overwrites if it does)
fs.writeFileSync("notes.txt", "Hello from Node!");

// Read a file
const contents = fs.readFileSync("notes.txt", "utf8");
console.log(contents); // Hello from Node!
```

("Sync" means the program pauses until the operation finishes — simple to reason about, fine for small scripts and learning; servers typically use async versions for performance, which you'll see naturally once you reach Express.)

### Your Own Modules: `require` and `module.exports`

You can split code across files and share functions between them:

```javascript
// math-helpers.js
function add(a, b) {
  return a + b;
}

module.exports = { add };
```

```javascript
// app.js
const { add } = require("./math-helpers.js");
console.log(add(2, 3)); // 5
```

Note the `./` before a local file path — this tells Node "look in this folder," as opposed to looking for an installed package.

### Environment Variables

Programs often need configuration values that differ between machines, or secrets that must never be hard-coded into your files (database addresses, passwords, API keys). Node exposes these via `process.env`:

```javascript
console.log(process.env.PATH); // an existing system variable, just as an example
```

In Module 9, you'll create your own custom environment variables in a `.env` file (which you already correctly added to `.gitignore` in Module 06) using a package called `dotenv`.

---

## Step-by-Step Hands-On

### Step 1: Initialize the Node Project

In your terminal, inside the `taskflow` folder (the same one with `public/` in it):

```
npm init -y
```

The `-y` flag accepts all defaults instantly instead of asking you questions one by one. This creates `package.json` at your project root. Open it in VS Code and look at it — you'll see fields like `"name"`, `"version"`, `"main"`, and an empty `"scripts"` object.

### Step 2: Write Your First Node Script

1. Create a new file at the project root (not inside `public/`): `playground.js`
2. Paste this exact content:

```javascript
// playground.js - Node fundamentals practice (not part of the final app)

console.log("Hello from Node.js!");

// Built-in modules
const path = require("path");
const fs = require("fs");

const fullPath = path.join(__dirname, "notes.txt");
console.log("Will write to:", fullPath);

fs.writeFileSync(fullPath, "These are my Node.js practice notes.\n");

const contents = fs.readFileSync(fullPath, "utf8");
console.log("File contents:", contents);

// Custom module usage
const { add, multiply } = require("./math-helpers.js");
console.log("2 + 3 =", add(2, 3));
console.log("4 * 5 =", multiply(4, 5));

// process.env example
console.log("Node version via process:", process.version);
```

3. Create a second file at the project root: `math-helpers.js`
4. Paste this exact content:

```javascript
// math-helpers.js

function add(a, b) {
  return a + b;
}

function multiply(a, b) {
  return a * b;
}

module.exports = { add, multiply };
```

### Step 3: Run the Script

In your terminal, at the project root:

```
node playground.js
```

You should see, in order:
- `Hello from Node.js!`
- The full file path it will write to
- `File contents: These are my Node.js practice notes.`
- `2 + 3 = 5`
- `4 * 5 = 20`
- A Node version string

Also confirm a new file `notes.txt` was created in your project folder (visible in VS Code's Explorer).

### Step 4: Add a Custom npm Script

Real Node projects almost always define a shortcut to run their main file. Open `package.json` and find the `"scripts"` section. Edit it to look exactly like this (keep any other fields in the file untouched):

```json
"scripts": {
  "test": "echo \"Error: no test specified\" && exit 1",
  "playground": "node playground.js"
}
```

Save the file. Now you can run the same script with:

```
npm run playground
```

Confirm it produces identical output to Step 3. This `"scripts"` pattern is exactly what you'll use in Module 08 to start your real server with `npm start`.

### Step 5: Install Your First Real Package (Preview)

Let's confirm `npm install` works end-to-end, using a tiny, genuinely useful package called `nodemon` (it automatically restarts your server whenever you save a file — you'll use it starting in Module 08).

```
npm install --save-dev nodemon
```

`--save-dev` marks it as a development-only tool (not needed when the app actually runs in production — only while you're coding).

After it finishes:
1. Check `package.json` — a new `"devDependencies"` section now lists `nodemon`.
2. Confirm a `node_modules` folder now exists in your project (this is exactly why we already excluded it in `.gitignore`).
3. Run `git status` — confirm `node_modules/` does **not** appear as a change to commit (proof `.gitignore` is working).

---

## Practice Exercises

1. Add a third function `subtract(a, b)` to `math-helpers.js`, export it, and call it from `playground.js` with two example numbers.
2. Modify `playground.js` to append (rather than overwrite) a second line to `notes.txt` using `fs.appendFileSync(fullPath, "Another line.\n")`. Run it twice and open `notes.txt` to confirm it now has multiple lines.
3. Add a second custom script to `package.json`'s `"scripts"` section, e.g. `"hello": "node -e \"console.log('hi there')\""`, then run it with `npm run hello`.

---

## Quiz

1. What does Node.js let JavaScript do that it normally cannot do in a browser?
2. What is `package.json` for?
3. What flag installs a package as a development-only dependency?
4. What does `require("./math-helpers.js")` do differently than `require("path")`?
5. Why is `node_modules` excluded from Git?
6. What terminal command did you add to `package.json` to create a shortcut, and what command runs it?
7. What does `module.exports` do?

---

## Module Checklist

- [ ] `package.json` exists at the project root with a working `"scripts"` section.
- [ ] `playground.js` and `math-helpers.js` run successfully and produce the expected console output.
- [ ] `notes.txt` was created by your script.
- [ ] `nodemon` is installed and listed under `devDependencies`, and `node_modules/` is correctly ignored by Git.
- [ ] I completed all 3 practice exercises.

---

## What's Next

Open `08-expressjs-fundamentals.md` — you'll use everything from this module to build TaskFlow's actual web server.

---

## Quiz Answer Key

1. Access system-level capabilities like reading/writing files on disk, opening network ports, and connecting to databases — things browsers block for security reasons.
2. It's the project's identity card: it records the project's name/version, its package dependencies, and custom command shortcuts (`scripts`), among other metadata.
3. `--save-dev`.
4. `require("./math-helpers.js")` loads your own local file (the `./` means "relative to this file's folder"); `require("path")` loads one of Node's built-in modules by name, with no path needed.
5. It contains downloaded third-party packages that are large, not authored by you, and are exactly reproducible from `package.json`/`package-lock.json` — so committing them would bloat the repository for no benefit.
6. Added `"playground": "node playground.js"` under `"scripts"`; ran it with `npm run playground`.
7. It defines what values/functions a file makes available to other files that `require()` it.
