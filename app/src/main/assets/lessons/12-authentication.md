# Module 12: User Accounts & Authentication

## Learning Objectives

By the end of this module you will be able to:
- Explain why passwords must never be stored as plain text, and how hashing fixes this.
- Implement registration and login using bcrypt and sessions.
- Protect routes so only logged-in users can reach them.
- Ensure each user only ever sees and modifies their own tasks.
- Organize routes into separate files using Express Router.

## Estimated Time
5–8 hours. This is the most security-sensitive module in the course — go slowly.

---

## Concepts

### Why Plain-Text Passwords Are Never Acceptable

If you store a user's password exactly as they typed it, anyone who gains access to your database (a hacker, a careless backup, a malicious insider) instantly has every user's real password — and since people reuse passwords across sites, that's catastrophic. The fix is **hashing**.

### Hashing with bcrypt

A hash function turns input text into a fixed-length, scrambled output that cannot practically be reversed back into the original text.

```javascript
const bcrypt = require("bcrypt");

const hashed = await bcrypt.hash("myPlainPassword", 10);
// hashed looks like: $2b$10$N9qo8uLOickgx2ZMRZoMy...
```

- The `10` is the **salt rounds** — how computationally expensive the hashing process is. Higher = slower to compute, but more resistant to brute-force attacks. `10` is a sensible, widely-used default.
- You **never store the original password**, only `hashed`.
- To check a login attempt, you don't "unhash" anything — you hash the new attempt's logic differently: bcrypt re-derives and compares safely:

```javascript
const matches = await bcrypt.compare("myPlainPassword", hashed); // true or false
```

`bcrypt.compare` is specifically designed so this comparison is safe and correct even though the original password is never recoverable.

### Sessions and Cookies (the approach this course uses)

After a successful login, the server needs a way to "remember" that this particular browser is now logged in, on every subsequent request — since HTTP requests are otherwise independent and stateless.

**Sessions** solve this: the server creates a small record of "who is logged in" (kept server-side, e.g. in memory for this course), and sends the browser a **cookie** containing just a random session ID. The browser automatically resends that cookie with every future request to the same site, and the server looks up the session by that ID to know who's making the request — all handled for you by the `express-session` package.

(The other common approach industry-wide is **JWT** — JSON Web Tokens, a self-contained signed token the client stores and sends manually with each request. Sessions are used in this course because the cookie mechanism is automatic and requires less manual frontend bookkeeping, which keeps focus on core concepts first.)

### Authorization vs. Authentication

- **Authentication** — verifying *who* someone is (login).
- **Authorization** — verifying *what* they're allowed to do (e.g., only delete their own tasks, not someone else's).

This module implements both: authentication via login/sessions, and authorization by tying every task to the `userId` of whoever created it, and filtering/checking ownership on every task operation.

### Express Router — Organizing Routes Into Files

As an app grows, keeping every route in one `server.js` file becomes unwieldy. `express.Router()` lets you define a group of related routes in their own file, then "mount" that group onto a path prefix in `server.js`:

```javascript
// in routes/exampleRoutes.js
const router = express.Router();
router.get("/foo", handler); // becomes whatever prefix it's mounted at, + "/foo"
module.exports = router;

// in server.js
app.use("/example", exampleRoutes); // now reachable at /example/foo
```

---

## Step-by-Step Hands-On

### Step 1: Install New Packages

```
npm install bcrypt express-session
```

### Step 2: Add a Session Secret to `.env`

Open `.env` and add a new line (any long, random string works — this is used to cryptographically sign the session cookie so it can't be tampered with):

```
SESSION_SECRET=change-this-to-a-long-random-string-1234567890
```

### Step 3: Create the User Model

Create `models/User.js`:

```javascript
// models/User.js

const mongoose = require("mongoose");

const userSchema = new mongoose.Schema({
  username: {
    type: String,
    required: true,
    unique: true,
    trim: true,
    minlength: 3
  },
  password: {
    type: String,
    required: true // this will always be a bcrypt HASH, never plain text
  }
}, { timestamps: true });

module.exports = mongoose.model("User", userSchema);
```

### Step 4: Add `userId` to the Task Model

Open `models/Task.js` and add a new field inside the schema (anywhere among the existing fields), so the schema becomes:

```javascript
const taskSchema = new mongoose.Schema({
  title: {
    type: String,
    required: true,
    trim: true
  },
  description: {
    type: String,
    default: ""
  },
  completed: {
    type: Boolean,
    default: false
  },
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User",
    required: true
  }
}, {
  timestamps: true
});
```

**Important:** any tasks created in earlier modules don't have a `userId` and will become inaccessible once we filter by it below. Clear them out now via `mongosh`:
```javascript
use taskflow_db
db.tasks.deleteMany({})
```

### Step 5: Create the Auth-Check Middleware

Create a new folder `middleware`, then `middleware/requireAuth.js`:

```javascript
// middleware/requireAuth.js
// Blocks any request that doesn't have a logged-in session.

function requireAuth(req, res, next) {
  if (!req.session.userId) {
    return res.status(401).json({ error: "You must be logged in to do that." });
  }
  next();
}

module.exports = requireAuth;
```

### Step 6: Create the Auth Routes

Create a new folder `routes`, then `routes/authRoutes.js`:

```javascript
// routes/authRoutes.js

const express = require("express");
const bcrypt = require("bcrypt");
const User = require("../models/User");
const requireAuth = require("../middleware/requireAuth");

const router = express.Router();

// POST /api/auth/register
router.post("/register", async (req, res) => {
  try {
    const { username, password } = req.body;

    if (!username || !password) {
      return res.status(400).json({ error: "Username and password are required." });
    }
    if (password.length < 6) {
      return res.status(400).json({ error: "Password must be at least 6 characters." });
    }

    const existingUser = await User.findOne({ username });
    if (existingUser) {
      return res.status(409).json({ error: "That username is already taken." });
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const newUser = await User.create({ username, password: hashedPassword });

    req.session.userId = newUser._id; // log them in immediately after registering
    res.status(201).json({ username: newUser.username });
  } catch (error) {
    res.status(500).json({ error: "Registration failed." });
  }
});

// POST /api/auth/login
router.post("/login", async (req, res) => {
  try {
    const { username, password } = req.body;

    const user = await User.findOne({ username });
    if (!user) {
      return res.status(401).json({ error: "Invalid username or password." });
    }

    const passwordMatches = await bcrypt.compare(password, user.password);
    if (!passwordMatches) {
      return res.status(401).json({ error: "Invalid username or password." });
    }

    req.session.userId = user._id;
    res.json({ username: user.username });
  } catch (error) {
    res.status(500).json({ error: "Login failed." });
  }
});

// POST /api/auth/logout
router.post("/logout", (req, res) => {
  req.session.destroy(() => {
    res.status(204).send();
  });
});

// GET /api/auth/me - returns the currently logged-in user, or 401 if none
router.get("/me", requireAuth, async (req, res) => {
  const user = await User.findById(req.session.userId);
  res.json({ username: user.username });
});

module.exports = router;
```

Notice we deliberately return the **same** generic error message ("Invalid username or password.") whether the username doesn't exist or the password is wrong — never reveal which one was incorrect, since that would let an attacker discover which usernames exist on your system.

### Step 7: Move Task Routes Into Their Own File, Protected and Scoped Per-User

Create `routes/taskRoutes.js`:

```javascript
// routes/taskRoutes.js

const express = require("express");
const Task = require("../models/Task");
const requireAuth = require("../middleware/requireAuth");

const router = express.Router();

router.use(requireAuth); // every route below this line requires login

// GET /api/tasks - only THIS user's tasks
router.get("/", async (req, res) => {
  try {
    const tasks = await Task.find({ userId: req.session.userId }).sort({ createdAt: -1 });
    res.json(tasks);
  } catch (error) {
    res.status(500).json({ error: "Failed to fetch tasks" });
  }
});

// GET /api/tasks/:id - only if it belongs to THIS user
router.get("/:id", async (req, res) => {
  try {
    const task = await Task.findOne({ _id: req.params.id, userId: req.session.userId });
    if (!task) return res.status(404).json({ error: "Task not found" });
    res.json(task);
  } catch (error) {
    res.status(400).json({ error: "Invalid task id" });
  }
});

// POST /api/tasks - automatically tagged with THIS user's id
router.post("/", async (req, res) => {
  try {
    const { title, description } = req.body;
    if (!title) return res.status(400).json({ error: "Title is required" });
    const newTask = await Task.create({
      title,
      description,
      userId: req.session.userId
    });
    res.status(201).json(newTask);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// PUT /api/tasks/:id - only updates if it belongs to THIS user
router.put("/:id", async (req, res) => {
  try {
    const updatedTask = await Task.findOneAndUpdate(
      { _id: req.params.id, userId: req.session.userId },
      req.body,
      { new: true, runValidators: true }
    );
    if (!updatedTask) return res.status(404).json({ error: "Task not found" });
    res.json(updatedTask);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// DELETE /api/tasks/:id - only deletes if it belongs to THIS user
router.delete("/:id", async (req, res) => {
  try {
    const deletedTask = await Task.findOneAndDelete({
      _id: req.params.id,
      userId: req.session.userId
    });
    if (!deletedTask) return res.status(404).json({ error: "Task not found" });
    res.status(204).send();
  } catch (error) {
    res.status(400).json({ error: "Invalid task id" });
  }
});

module.exports = router;
```

Notice every single query now includes `userId: req.session.userId` in its filter — this is what makes it impossible for User A to read, edit, or delete User B's tasks, even if User A guesses or already knows a valid task `_id`.

### Step 8: Rewrite `server.js` to Use Sessions and the New Route Files

Replace the **entire contents** of `server.js` with this:

```javascript
// server.js - TaskFlow backend server

require("dotenv").config();

const express = require("express");
const session = require("express-session");
const connectDB = require("./config/db");
const authRoutes = require("./routes/authRoutes");
const taskRoutes = require("./routes/taskRoutes");

const app = express();
const PORT = process.env.PORT || 3000;

connectDB();

// ---------- Middleware ----------
app.use(express.json());
app.use(express.static("public"));

app.use((req, res, next) => {
  console.log(`${new Date().toISOString()} - ${req.method} ${req.url}`);
  next();
});

app.use(session({
  secret: process.env.SESSION_SECRET,
  resave: false,
  saveUninitialized: false,
  cookie: {
    maxAge: 1000 * 60 * 60 * 24 // 1 day, in milliseconds
  }
}));

// ---------- Routes ----------
app.use("/api/auth", authRoutes);
app.use("/api/tasks", taskRoutes);

// ---------- Start Server ----------
app.listen(PORT, () => {
  console.log(`TaskFlow server running at http://localhost:${PORT}`);
});
```

### Step 9: Replace the Entire Contents of `public/app.js`

This adds real login/register handling, a logged-in check that protects the dashboard, and a real logout. Replace the **entire file** with this:

```javascript
// app.js - TaskFlow frontend logic (with authentication)

console.log("app.js loaded successfully.");

const API_BASE = "/api/tasks";

// ---------------- Login / Register (index.html) ----------------

const loginForm = document.getElementById("login-form");
const registerForm = document.getElementById("register-form");

if (loginForm) {
  loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const username = document.getElementById("login-username").value.trim();
    const password = document.getElementById("login-password").value;
    const errorEl = document.getElementById("login-error");
    errorEl.textContent = "";

    try {
      const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
      });
      const data = await response.json();

      if (!response.ok) {
        errorEl.textContent = data.error || "Login failed.";
        return;
      }
      window.location.href = "dashboard.html";
    } catch (error) {
      errorEl.textContent = "Something went wrong. Please try again.";
    }
  });
}

if (registerForm) {
  registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const username = document.getElementById("register-username").value.trim();
    const password = document.getElementById("register-password").value;
    const errorEl = document.getElementById("register-error");
    errorEl.textContent = "";

    try {
      const response = await fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
      });
      const data = await response.json();

      if (!response.ok) {
        errorEl.textContent = data.error || "Registration failed.";
        return;
      }
      window.location.href = "dashboard.html";
    } catch (error) {
      errorEl.textContent = "Something went wrong. Please try again.";
    }
  });
}

// ---------------- Dashboard (dashboard.html) ----------------

const taskForm = document.getElementById("task-form");
const taskList = document.getElementById("task-list");
const logoutButton = document.getElementById("logout-button");
const welcomeMessage = document.getElementById("welcome-message");

// Confirm the user is actually logged in before showing the dashboard
async function checkAuthAndInit() {
  try {
    const response = await fetch("/api/auth/me");
    if (!response.ok) {
      window.location.href = "index.html"; // not logged in -> back to login
      return;
    }
    const data = await response.json();
    if (welcomeMessage) {
      welcomeMessage.textContent = `Welcome, ${data.username}`;
    }
    loadTasks();
  } catch (error) {
    window.location.href = "index.html";
  }
}

async function loadTasks() {
  try {
    const response = await fetch(API_BASE);
    if (!response.ok) throw new Error("Failed to load tasks");
    const tasks = await response.json();
    renderTasks(tasks);
  } catch (error) {
    console.error(error);
  }
}

function renderTasks(tasks) {
  taskList.innerHTML = "";
  if (tasks.length === 0) {
    taskList.innerHTML = "<li>No tasks yet. Add one above!</li>";
    return;
  }
  for (const task of tasks) {
    const li = document.createElement("li");
    li.className = "task-item" + (task.completed ? " completed" : "");
    li.dataset.id = task._id;
    li.innerHTML = `
      <label>
        <input type="checkbox" class="task-checkbox" ${task.completed ? "checked" : ""}>
        <strong>${escapeHtml(task.title)}</strong>
        ${task.description ? `- ${escapeHtml(task.description)}` : ""}
      </label>
      <button class="delete-button" type="button">Delete</button>
    `;
    taskList.appendChild(li);
  }
}

function escapeHtml(text) {
  const div = document.createElement("div");
  div.textContent = text;
  return div.innerHTML;
}

if (taskForm) {
  taskForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const titleInput = document.getElementById("task-title");
    const descriptionInput = document.getElementById("task-description");
    const title = titleInput.value.trim();
    const description = descriptionInput.value.trim();
    if (!title) return;

    try {
      const response = await fetch(API_BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, description })
      });
      if (!response.ok) throw new Error("Failed to create task");
      titleInput.value = "";
      descriptionInput.value = "";
      loadTasks();
    } catch (error) {
      console.error(error);
      alert("Could not add task. Please try again.");
    }
  });
}

if (taskList) {
  taskList.addEventListener("click", async (event) => {
    const li = event.target.closest(".task-item");
    if (!li) return;
    const id = li.dataset.id;

    if (event.target.classList.contains("delete-button")) {
      try {
        const response = await fetch(`${API_BASE}/${id}`, { method: "DELETE" });
        if (!response.ok && response.status !== 204) throw new Error("Failed to delete task");
        loadTasks();
      } catch (error) {
        console.error(error);
        alert("Could not delete task.");
      }
    }
  });

  taskList.addEventListener("change", async (event) => {
    if (!event.target.classList.contains("task-checkbox")) return;
    const li = event.target.closest(".task-item");
    const id = li.dataset.id;
    const completed = event.target.checked;

    try {
      const response = await fetch(`${API_BASE}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ completed })
      });
      if (!response.ok) throw new Error("Failed to update task");
      loadTasks();
    } catch (error) {
      console.error(error);
      alert("Could not update task.");
    }
  });
}

if (logoutButton) {
  logoutButton.addEventListener("click", async () => {
    await fetch("/api/auth/logout", { method: "POST" });
    window.location.href = "index.html";
  });
}

// Entry point: only run the dashboard auth check on the dashboard page
if (taskList) {
  checkAuthAndInit();
}
```

### Step 10: Test the Complete Authentication Flow

1. Restart your server: `npm run dev`.
2. Go to `http://localhost:3000/dashboard.html` **directly, without logging in first.** Confirm you are immediately redirected to `index.html` — proving the protection works.
3. On `index.html`, register a new account (e.g., username `alice`, password `password123`).
4. Confirm you're redirected to `dashboard.html`, and it shows `Welcome, alice`.
5. Add 2–3 tasks as `alice`.
6. Click **Log Out**. Confirm you're returned to `index.html`.
7. Try going directly to `dashboard.html` again — confirm you're redirected back to `index.html` (the session is gone).
8. Register a **second** account (e.g., `bob` / `password456`). Confirm Bob's dashboard shows **zero** tasks — not Alice's.
9. Add one task as Bob. Log out. Log back in as `alice`. Confirm Alice still sees only her own original 2–3 tasks, and not Bob's.
10. Try registering `alice` again (duplicate username) — confirm you get a clear error message instead of a crash or silent failure.
11. Try logging in as `alice` with the wrong password — confirm you get "Invalid username or password," not a server crash.

---

## Practice Exercises

1. In `mongosh`, run `db.users.find().pretty()` inside `taskflow_db` and confirm the `password` field is a long bcrypt hash, never your real typed password.
2. Add a minimum-length check on the frontend too (in `app.js`, before sending the request) so the user gets instant feedback without waiting for a server round-trip — but keep the backend check as well. (This illustrates a real principle: always validate on the server, since frontend validation can be bypassed, but also validate on the frontend for a faster, friendlier user experience.)
3. Using Thunder Client, try calling `GET http://localhost:3000/api/tasks` with no login at all (Thunder Client won't have your browser's cookies) and confirm you get a `401` response — proof the backend itself is protected, independent of the frontend.

---

## Quiz

1. Why is hashing different from encryption, in terms of being reversible?
2. What does the number `10` represent in `bcrypt.hash(password, 10)`?
3. Why do both the "wrong password" and "username not found" cases return the exact same error message?
4. What is stored in the browser's cookie — the user's identity directly, or something else?
5. What is the difference between authentication and authorization?
6. In `taskRoutes.js`, what specific part of each database query is what prevents one user from accessing another user's tasks?
7. What status code does `requireAuth` send when there's no valid session, and what does that code mean?
8. Why did we need to delete old test tasks from the database in Step 4?

---

## Module Checklist

- [ ] `bcrypt` and `express-session` are installed.
- [ ] `.env` includes a `SESSION_SECRET`.
- [ ] `models/User.js` and the updated `models/Task.js` (with `userId`) exist.
- [ ] `middleware/requireAuth.js`, `routes/authRoutes.js`, and `routes/taskRoutes.js` exist.
- [ ] `server.js` and `app.js` were fully replaced with the new versions.
- [ ] I completed the full 11-step test flow with two separate accounts and confirmed task isolation between them.
- [ ] I completed all 3 practice exercises.

---

## What's Next

Open `13-testing-debugging.md` to learn structured ways to verify your app works and to track down bugs efficiently, rather than guessing.

---

## Quiz Answer Key

1. Encryption is designed to be reversible (decrypted back to the original with the right key); hashing is one-way by design — there is no key or process that turns a bcrypt hash back into the original password, which is exactly why it's safe to store.
2. The number of "salt rounds" — how many times the hashing algorithm internally repeats its work, controlling how computationally expensive (and brute-force-resistant) the hash is to compute.
3. To avoid revealing to an attacker whether a given username exists in the system at all — a different message for each case would let someone enumerate valid usernames.
4. Only a random session ID (a reference), which the server uses to look up the corresponding session data server-side; the user's actual identity/data is not stored directly inside the cookie in this approach.
5. Authentication verifies *who* a user is (login); authorization verifies *what* that authenticated user is allowed to do or access (e.g., only their own tasks).
6. The `userId: req.session.userId` condition included in every `find`, `findOne`, `findOneAndUpdate`, and `findOneAndDelete` filter, which restricts every operation to documents owned by the currently logged-in user.
7. `401 Unauthorized` — meaning the request lacks valid authentication credentials for the requested resource.
8. Because they were created before the `userId` field existed and required; once task routes started filtering by `userId`, those old tasks (with no `userId`) would never match any user's filter and would become permanently inaccessible clutter.
