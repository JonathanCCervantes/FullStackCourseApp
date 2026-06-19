# Module 13: Testing & Debugging

## Learning Objectives

By the end of this module you will be able to:
- Read and interpret a Node.js error/stack trace.
- Use breakpoints in VS Code's debugger instead of guessing with `console.log`.
- Recognize and fix common errors specific to this stack.
- Systematically test every API endpoint with Thunder Client.
- Write and run a basic automated test using Node's built-in test runner.

## Estimated Time
3–5 hours.

---

## Concepts

### Reading a Stack Trace

When Node hits an error it can't recover from, it prints a **stack trace** — the error message plus the chain of function calls that led to it, most-recent-call-first.

Example:
```
TypeError: Cannot read properties of undefined (reading 'title')
    at /Users/you/taskflow/routes/taskRoutes.js:25:34
    at processTicksAndRejections (node:internal/process/task_queues:95:5)
```

How to read it:
1. **The message** (`TypeError: Cannot read properties of undefined...`) tells you the *kind* of mistake.
2. **The first file/line** (`taskRoutes.js:25:34`) tells you exactly where it happened — line 25, character 34 of that file. **Always look here first.**
3. Lines further down are usually internal Node machinery — rarely useful for your own bugs, but occasionally show you which of *your* functions called the one that failed (a "call stack").

### `console.log` Debugging (Useful, But Limited)

Sprinkling `console.log(variableName)` around your code to see what's actually happening is a completely legitimate, common technique — especially for quick checks. Its downside: you have to guess in advance what to print, and remove the logs afterward.

### Breakpoint Debugging in VS Code (More Powerful)

A breakpoint pauses your program at an exact line, letting you inspect every variable's actual live value at that moment — no guessing, no edits to your code required.

**Setup:**
1. In VS Code, open `server.js` (or any route file).
2. Click in the empty space just to the left of a line number, on a line inside a route handler (e.g., the first line inside `router.post("/", async (req, res) => { ... })` in `taskRoutes.js`). A red dot appears — that's your breakpoint.
3. Click the **Run and Debug** icon in the left sidebar (a play button with a bug).
4. Click **"create a launch.json file"** if prompted, choose **Node.js** as the environment.
5. Click the green **Run** button (or press `F5`).
6. Your server starts under the debugger. Trigger the route you set a breakpoint on (e.g., send a `POST /api/tasks` request via Thunder Client).
7. VS Code pauses execution exactly at your breakpoint. The left panel now shows every local variable's current value (`req.body`, etc.). Hover over any variable in the code itself to see its value inline too.
8. Use the floating debug toolbar to: **Step Over** (run the next line), **Continue** (run until the next breakpoint or completion), or **Stop**.

This is the single most effective tool for understanding "what is actually happening, right now" in your code, and is worth genuinely practicing rather than skipping.

### Common Errors in This Stack, and Their Fixes

| Error | Typical Cause | Fix |
|---|---|---|
| `EADDRINUSE: address already in use :::3000` | Another running process (often a previous, not-fully-stopped server) is already using port 3000 | Find and stop the old process, or change `PORT` in `.env` temporarily |
| `MongoServerError: E11000 duplicate key error` | Trying to insert a document violating a `unique` schema field (e.g., a username that already exists) | Catch this specific case and return a friendly error instead of crashing — already handled in Module 12's registration route |
| `Cannot read properties of undefined (reading 'X')` | Trying to access a property on something that is `undefined`/`null` — often because data didn't load yet, or a field name was typo'd | Add a `console.log` or breakpoint just before that line to confirm the actual value; check for typos in field names |
| `MongoNetworkError` / connection refused | MongoDB isn't running | Revisit Module 02 Step 5 / Module 09 Practice Exercise 3 to start the MongoDB service |
| Request hangs forever, never responds | A middleware function forgot to call `next()`, or a route forgot to call any `res.` method | Check every middleware for a `next()` call; check every route branch actually sends a response |
| `401 Unauthorized` when you expect to be logged in | Session cookie not being sent/received — often because you tested an authenticated route directly via a tool that doesn't share your browser's cookies | This is expected behavior when testing with Thunder Client without a prior login request in the same client session; log in via the form in the browser to test authenticated dashboard behavior |
| Frontend changes don't appear after saving | Browser cache, or you edited the wrong file copy | Hard-refresh the browser (`Ctrl+Shift+R` / `Cmd+Shift+R`); confirm Live Server / nodemon actually restarted |

### Manual Testing Checklist (Systematic, Not Random Clicking)

Random clicking misses edge cases. A written checklist, run every time you make a meaningful change, catches regressions. Example for the task API:

- [ ] Can create a task with valid data → `201`
- [ ] Can create a task with missing title → `400`
- [ ] Can fetch all tasks for the logged-in user only
- [ ] Can fetch a single task by valid id
- [ ] Fetching a task by invalid/malformed id → `400`, not a crash
- [ ] Fetching another user's task by id → `404` (not found, not "forbidden" — deliberately not confirming the task exists at all to that user)
- [ ] Can update a task's `completed` status
- [ ] Can delete a task → `204`
- [ ] Deleting an already-deleted/nonexistent task → `404`
- [ ] All of the above while logged out → `401`

### Automated Testing (A First Taste)

Manually re-clicking through a checklist every time doesn't scale. **Automated tests** are code that tests your code, that you can re-run instantly, as often as you want, for free. Node.js includes a built-in test runner (`node:test`) requiring zero extra installation.

```javascript
const test = require("node:test");
const assert = require("node:assert");

test("adds two numbers correctly", () => {
  const result = 2 + 2;
  assert.strictEqual(result, 4);
}
```

`assert.strictEqual(actual, expected)` throws an error (failing the test) if the two values aren't exactly equal — otherwise the test silently passes.

---

## Step-by-Step Hands-On

### Step 1: Deliberately Break Something and Read the Trace

1. In `routes/taskRoutes.js`, temporarily change one line in the `POST /` handler from:
   ```javascript
   const { title, description } = req.body;
   ```
   to:
   ```javascript
   const { title, description } = req.bodyy; // intentional typo
   ```
2. Save, and send a `POST /api/tasks` request via Thunder Client while logged in (via the browser) with a valid body.
3. Look at your terminal. You should see a stack trace mentioning `Cannot read properties of undefined`. Find the exact file and line number it reports.
4. Fix the typo back to `req.body`, save, and confirm the route works again.

### Step 2: Practice Breakpoint Debugging

1. Follow the **Setup** instructions under "Breakpoint Debugging" above, placing a breakpoint on the `const { title, description } = req.body;` line in `routes/taskRoutes.js`.
2. Start debugging (`F5`), log in via the browser, and submit the "Add Task" form on the dashboard.
3. Confirm execution pauses at your breakpoint, and inspect `req.body` in the Variables panel — confirm it shows the exact title/description you typed.
4. Click **Continue** and confirm the task is created normally afterward.
5. Remove the breakpoint (click the red dot again) when done.

### Step 3: Build a Thunder Client Collection for Regression Testing

1. In Thunder Client, click **Collections** → **New Collection**, name it `TaskFlow API`.
2. Inside it, save one request per checklist item from the "Manual Testing Checklist" above that involves the API directly (you can save a request by clicking the save icon after configuring it). At minimum, save: register, login, get all tasks, create task (valid), create task (missing title), get one invalid id, delete task, logout.
3. Run through the entire saved collection top-to-bottom after making this module's changes, confirming every response matches what's expected.

This collection is now reusable forever — any time you change backend code, you can re-run it in a couple of minutes instead of re-testing by memory.

### Step 4: Write Your First Automated Tests

1. Create a new folder `utils`, then `utils/validators.js` with this exact content (small, pure, easily-testable functions extracted from logic you've already written):

```javascript
// utils/validators.js

function isValidUsername(username) {
  return typeof username === "string" && username.trim().length >= 3;
}

function isValidPassword(password) {
  return typeof password === "string" && password.length >= 6;
}

module.exports = { isValidUsername, isValidPassword };
```

2. Create a new folder `tests`, then `tests/validators.test.js` with this exact content:

```javascript
// tests/validators.test.js

const test = require("node:test");
const assert = require("node:assert");
const { isValidUsername, isValidPassword } = require("../utils/validators");

test("isValidUsername accepts a normal username", () => {
  assert.strictEqual(isValidUsername("alice"), true);
});

test("isValidUsername rejects a too-short username", () => {
  assert.strictEqual(isValidUsername("al"), false);
});

test("isValidUsername rejects non-string input", () => {
  assert.strictEqual(isValidUsername(12345), false);
});

test("isValidPassword accepts a 6+ character password", () => {
  assert.strictEqual(isValidPassword("abcdef"), true);
});

test("isValidPassword rejects a too-short password", () => {
  assert.strictEqual(isValidPassword("abc"), false);
});
```

3. Add a test script to `package.json`'s `"scripts"` section:
   ```json
   "test": "node --test"
   ```
4. Run it:
   ```
   npm test
   ```
5. Confirm the output shows all 5 tests passing (look for `pass 5` in the summary).
6. Now intentionally break one: change `isValidUsername`'s `>= 3` to `>= 30`, save, and re-run `npm test`. Confirm the "accepts a normal username" test now fails, with a clear message showing expected vs. actual. Fix it back to `3` and confirm all tests pass again.

### Step 5 (Optional but Recommended): Use the Validators For Real

Update `routes/authRoutes.js`'s `/register` handler to use them instead of the inline checks, replacing:
```javascript
if (!username || !password) {
  return res.status(400).json({ error: "Username and password are required." });
}
if (password.length < 6) {
  return res.status(400).json({ error: "Password must be at least 6 characters." });
}
```
with:
```javascript
const { isValidUsername, isValidPassword } = require("../utils/validators");
// (add this require near the top of the file with the others)

if (!isValidUsername(username)) {
  return res.status(400).json({ error: "Username must be at least 3 characters." });
}
if (!isValidPassword(password)) {
  return res.status(400).json({ error: "Password must be at least 6 characters." });
}
```
This is a real, common pattern: logic you can unit-test in isolation (fast, no server/database needed) backing the logic your live routes depend on.

---

## Practice Exercises

1. Add one more test to `tests/validators.test.js` checking that `isValidUsername("   ")` (only whitespace) correctly returns `false`.
2. Deliberately stop your MongoDB service (as in Module 09's practice), try registering a user, and read the resulting terminal error — identify which row of the "Common Errors" table it matches.
3. Add 3 more checklist items of your own to the "Manual Testing Checklist" above (e.g., around the `completed` toggle, or duplicate registration) and save matching requests into your Thunder Client collection.

---

## Quiz

1. When reading a stack trace, which line should you look at first?
2. What is the main advantage of a breakpoint over a `console.log` statement?
3. What does `assert.strictEqual(actual, expected)` do if the values don't match?
4. Why is it valuable to extract small functions like `isValidUsername` into their own file, separate from the route handler?
5. What does `EADDRINUSE` mean, in your own words?
6. Why does Module 12 deliberately return `404` (not `403 Forbidden`) when a user requests another user's task by id?
7. What command runs the automated tests in this project?

---

## Module Checklist

- [ ] I intentionally broke a route, read the resulting stack trace, and located the exact line.
- [ ] I successfully used a VS Code breakpoint to inspect `req.body` mid-request.
- [ ] I built a Thunder Client collection covering at least 7 checklist scenarios.
- [ ] `utils/validators.js` and `tests/validators.test.js` exist, and `npm test` passes.
- [ ] I watched a test fail on purpose, understood the failure message, then fixed it.
- [ ] I completed all 3 practice exercises.

---

## What's Next

Open `14-capstone-project.md` to review, polish, and extend the complete TaskFlow application — the final assembly of everything you've built.

---

## Quiz Answer Key

1. The first line that points to a file inside your own project (as opposed to internal Node/library files) — this is almost always where your actual mistake lives.
2. A breakpoint lets you pause execution and inspect the *actual live values* of every variable in scope at that exact moment, without having to predict in advance what you need to see or edit your code to add print statements.
3. It throws an error, which causes that test to fail and report the mismatch between the expected and actual values.
4. It lets you test that logic in isolation, quickly and without needing a running server or database, and makes the logic reusable anywhere else it's needed (e.g., both backend validation and, with adaptation, frontend checks).
5. It means another running process is already using the port your server is trying to listen on, so the new server can't start until that port is freed or a different port is used.
6. To avoid confirming to an unauthorized requester that a task with that id even exists at all — returning `403` would leak that information, while `404` reveals nothing about whether the resource exists.
7. `npm test`.
