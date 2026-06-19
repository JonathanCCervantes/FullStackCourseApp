# Module 08: Express.js Fundamentals

## Learning Objectives

By the end of this module you will be able to:
- Explain what Express is and why it's used on top of Node.js.
- Create a server with routes for different HTTP methods and URLs.
- Explain and use middleware, including built-in and third-party middleware.
- Serve your `public/` frontend files through your own server.
- Build a working (temporary, in-memory) REST API for tasks.

## Estimated Time
3–5 hours.

---

## Concepts

### What Is Express?

Node.js alone can create a web server, but it requires a lot of manual, repetitive code for routing, parsing request bodies, etc. **Express** is a minimal framework built on top of Node that makes this dramatically simpler and is the most widely used Node web framework.

### The Express App Object

```javascript
const express = require("express");
const app = express();

app.listen(3000, () => {
  console.log("Server running on http://localhost:3000");
});
```

`app.listen(port, callback)` starts the server, listening for incoming requests on that port number on your own computer.

### Routes

A route matches an HTTP method + URL path to a handler function:

```javascript
app.get("/hello", (req, res) => {
  res.send("Hello, world!");
});
```

- `req` (request) — information about the incoming request (URL params, query string, body, headers).
- `res` (response) — methods to send something back (`res.send()`, `res.json()`, `res.status()`).

Common response methods:

```javascript
res.send("plain text or HTML");
res.json({ key: "value" });          // sends JSON, sets correct headers automatically
res.status(404).json({ error: "Not found" }); // chain a status code before sending
```

### Route Parameters and Query Strings

```javascript
// Route parameter: /tasks/42  ->  req.params.id === "42"
app.get("/tasks/:id", (req, res) => {
  res.json({ requestedId: req.params.id });
});

// Query string: /search?term=milk  ->  req.query.term === "milk"
app.get("/search", (req, res) => {
  res.json({ searchedFor: req.query.term });
});
```

### Middleware — the Most Important Express Concept

Middleware are functions that run **in between** the request arriving and your route handler responding. They can inspect/modify the request, end the response early, or pass control onward with `next()`.

```javascript
function logger(req, res, next) {
  console.log(`${req.method} ${req.url}`);
  next(); // IMPORTANT: without this, the request hangs forever
}

app.use(logger); // applies to every request
```

Middleware is how Express handles cross-cutting concerns cleanly: logging, parsing request bodies, authentication checks (Module 12), error handling, and serving static files — all implemented as middleware.

### Built-in Middleware You Need Immediately

```javascript
app.use(express.json());
```

This parses incoming requests whose body is JSON (e.g., from a `fetch` POST) into a regular JavaScript object, available at `req.body`. Without this line, `req.body` would be `undefined` and every form submission to your API would fail.

```javascript
app.use(express.static("public"));
```

This tells Express: "serve every file inside the `public/` folder directly, automatically, as if it were a static website." This is how your `index.html`, `dashboard.html`, `style.css`, and `app.js` from Modules 03–05 will finally be served by your own server.

### In-Memory Data (Temporary, Until Module 9–10)

Before connecting a real database, it's valuable to build and test routes using a plain JavaScript array as fake/temporary storage. This isolates "learning Express routing" from "learning MongoDB," one at a time.

```javascript
let tasks = [
  { id: 1, title: "Buy milk", completed: false },
  { id: 2, title: "Walk dog", completed: true }
];
```

**Important limitation to understand:** this array lives only in RAM. It resets to its starting value every time you restart the server. This is expected and fine for this module — Module 10 replaces it with permanent MongoDB storage using nearly identical route code.

---

## Step-by-Step Hands-On

### Step 1: Install Express

In your terminal, at the project root:

```
npm install express
```

Confirm it now appears under `"dependencies"` in `package.json` (note: not `devDependencies` — Express is needed when the app actually runs, not just while coding).

### Step 2: Create `server.js`

1. Create a file at the project root: `server.js`
2. Paste this exact content:

```javascript
// server.js - TaskFlow backend server

const express = require("express");
const app = express();
const PORT = 3000;

// ---------- Middleware ----------
app.use(express.json());           // parse JSON request bodies into req.body
app.use(express.static("public")); // serve index.html, dashboard.html, style.css, app.js

// Simple logging middleware (custom, written by us)
app.use((req, res, next) => {
  console.log(`${new Date().toISOString()} - ${req.method} ${req.url}`);
  next();
});

// ---------- Temporary in-memory data (replaced by MongoDB in Module 10) ----------
let tasks = [
  { id: 1, title: "Buy milk", description: "2% milk", completed: false },
  { id: 2, title: "Walk dog", description: "Around the block", completed: true }
];
let nextId = 3;

// ---------- Routes ----------

// GET /api/tasks - return all tasks
app.get("/api/tasks", (req, res) => {
  res.json(tasks);
});

// GET /api/tasks/:id - return one task by id
app.get("/api/tasks/:id", (req, res) => {
  const id = Number(req.params.id);
  const task = tasks.find((t) => t.id === id);

  if (!task) {
    return res.status(404).json({ error: "Task not found" });
  }
  res.json(task);
});

// POST /api/tasks - create a new task
app.post("/api/tasks", (req, res) => {
  const { title, description } = req.body;

  if (!title) {
    return res.status(400).json({ error: "Title is required" });
  }

  const newTask = {
    id: nextId++,
    title,
    description: description || "",
    completed: false
  };
  tasks.push(newTask);
  res.status(201).json(newTask);
});

// PUT /api/tasks/:id - update an existing task
app.put("/api/tasks/:id", (req, res) => {
  const id = Number(req.params.id);
  const task = tasks.find((t) => t.id === id);

  if (!task) {
    return res.status(404).json({ error: "Task not found" });
  }

  const { title, description, completed } = req.body;
  if (title !== undefined) task.title = title;
  if (description !== undefined) task.description = description;
  if (completed !== undefined) task.completed = completed;

  res.json(task);
});

// DELETE /api/tasks/:id - remove a task
app.delete("/api/tasks/:id", (req, res) => {
  const id = Number(req.params.id);
  const index = tasks.findIndex((t) => t.id === id);

  if (index === -1) {
    return res.status(404).json({ error: "Task not found" });
  }

  tasks.splice(index, 1);
  res.status(204).send(); // 204 = success, no content to return
});

// ---------- Start Server ----------
app.listen(PORT, () => {
  console.log(`TaskFlow server running at http://localhost:${PORT}`);
});
```

### Step 3: Add Start Scripts

Open `package.json`, and update the `"scripts"` section to exactly this (keep `"playground"` if you want, or remove it — your choice):

```json
"scripts": {
  "start": "node server.js",
  "dev": "nodemon server.js"
}
```

- `npm start` — runs the server normally.
- `npm run dev` — runs it via `nodemon`, which auto-restarts the server every time you save a file. Use this one while developing.

### Step 4: Run the Server

```
npm run dev
```

You should see: `TaskFlow server running at http://localhost:3000`, and the server keeps running (the terminal won't give you a new prompt — that's correct, it's actively listening). To stop it later, press `Ctrl+C` in that terminal.

### Step 5: Test It in the Browser

1. Open your browser and go to: `http://localhost:3000`
   - You should see your styled `index.html` (the login/register page) — now served by **your own server**, not by double-clicking the file. Notice your terminal logs this request, thanks to the logging middleware.
2. Go to: `http://localhost:3000/dashboard.html` — confirm it loads too.
3. Go to: `http://localhost:3000/api/tasks` — you should see raw JSON in the browser: the two starter tasks.
4. Go to: `http://localhost:3000/api/tasks/1` — you should see just that one task as JSON.
5. Go to: `http://localhost:3000/api/tasks/999` — you should see `{"error":"Task not found"}` and (check Developer Tools → Network tab) a `404` status code.

### Step 6: Test the Other Methods with Thunder Client

Browsers can only easily send `GET` requests by typing a URL. To test `POST`/`PUT`/`DELETE`, use the Thunder Client extension you installed in Module 02 (or install it now: Extensions icon in VS Code → search "Thunder Client" → Install).

1. In VS Code, click the Thunder Client icon in the left sidebar (lightning bolt).
2. Click **New Request**.
3. Set the method dropdown to `POST`, the URL to `http://localhost:3000/api/tasks`.
4. Click the **Body** tab, choose **JSON**, and enter:
   ```json
   {
     "title": "Read a book",
     "description": "30 minutes before bed"
   }
   ```
5. Click **Send**. You should get back status `201` and the new task with an auto-assigned `id`.
6. Reload `http://localhost:3000/api/tasks` in your browser — confirm the new task is now in the list (remember: this resets if you restart the server, since it's still in-memory).
7. Create a new Thunder Client request with method `DELETE` and URL `http://localhost:3000/api/tasks/1`. Send it, confirm status `204`, then reload `/api/tasks` and confirm task `1` is gone.

---

## Practice Exercises

1. Add a new route `GET /api/tasks/completed` that returns only tasks where `completed` is `true`, using `.filter()`. (Hint: define this route **before** `/api/tasks/:id` in your file — Express matches routes top-to-bottom, and `:id` would otherwise incorrectly try to match the literal word "completed.")
2. Using Thunder Client, send a `PUT` request to `/api/tasks/2` with body `{ "completed": false }` and confirm the response reflects the change.
3. Add a custom middleware function that rejects (`res.status(403).json({ error: "Forbidden" })`) any request whose URL includes the word `secret` — then test it by visiting `http://localhost:3000/secret` in your browser and confirming you get blocked.

---

## Quiz

1. What does `app.use(express.json())` enable that wouldn't work otherwise?
2. What does `app.use(express.static("public"))` do?
3. In Express middleware, what does calling `next()` do, and what happens if you forget it?
4. What HTTP status code did we send for "task not found," and what does it mean?
5. Why does route order matter when you have both `/api/tasks/completed` and `/api/tasks/:id`?
6. What is the current major limitation of this module's data storage, and which future module fixes it?
7. What's the difference between `npm start` and `npm run dev` in this project?

---

## Module Checklist

- [ ] Express is installed and listed in `package.json` dependencies.
- [ ] `server.js` runs successfully with `npm run dev`.
- [ ] Visiting `http://localhost:3000` shows your real `index.html`, served by your own server.
- [ ] All 5 routes (`GET` list, `GET` one, `POST`, `PUT`, `DELETE`) work, tested via browser and/or Thunder Client.
- [ ] I understand that current data resets on server restart, and why.
- [ ] I completed all 3 practice exercises.

---

## What's Next

Open `09-mongodb-fundamentals.md` to learn the database that will make your tasks survive a server restart — permanently.

---

## Quiz Answer Key

1. It enables `req.body` to contain the parsed JSON object sent in a request (e.g., from a `POST`); without it, `req.body` is `undefined`, breaking any route that depends on incoming data.
2. It serves every file inside the `public` folder automatically as static assets, so requests like `/index.html` or `/style.css` are answered directly from disk without you writing a route for each file.
3. `next()` passes control to the next middleware/route handler in the chain; forgetting it causes the request to hang indefinitely, since no response is ever sent.
4. `404`, meaning "Not Found" — the server understood the request but found no resource matching it.
5. Express matches routes in the order they're defined, top to bottom, and stops at the first match. If `/api/tasks/:id` came first, a request to `/api/tasks/completed` would incorrectly match it, treating `"completed"` as an `:id` value, so the more specific literal route must be defined first.
6. Data is stored only in memory (a plain array) and is lost whenever the server restarts; Module 10 fixes this by persisting data in MongoDB.
7. `npm start` runs the server once with plain `node`; `npm run dev` runs it through `nodemon`, which watches your files and automatically restarts the server whenever you save a change.
