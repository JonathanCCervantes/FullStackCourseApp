# Module 10: Connecting Express + MongoDB (Real, Permanent Task Storage)

## Learning Objectives

By the end of this module you will be able to:
- Define a Mongoose Schema and Model.
- Rewrite Express routes to perform real, permanent database operations.
- Use `async`/`await` with proper error handling in route handlers.
- Confirm that your data now survives a server restart.

## Estimated Time
3–5 hours.

---

## Concepts

### Schema vs. Model

- A **Schema** defines the shape of a document: which fields exist, their types, defaults, and validation rules.
- A **Model** is built from a schema and is the actual tool you use to create, read, update, and delete documents of that shape in MongoDB.

```javascript
const taskSchema = new mongoose.Schema({
  title: { type: String, required: true },
  description: { type: String, default: "" },
  completed: { type: Boolean, default: false }
});

const Task = mongoose.model("Task", taskSchema);
```

Mongoose automatically pluralizes and lowercases `"Task"` to determine the MongoDB collection name: `tasks` — matching what you used manually in Module 09's shell exercises.

### Core Mongoose Query Methods

| Method | Purpose |
|---|---|
| `Task.find(filter)` | Get all matching documents (no filter = all) |
| `Task.findById(id)` | Get one document by its `_id` |
| `Task.create(data)` | Insert a new document |
| `Task.findByIdAndUpdate(id, changes, options)` | Update one document by id |
| `Task.findByIdAndDelete(id)` | Delete one document by id |

All of these return Promises, so you use `await` with them inside `async` functions — exactly the pattern previewed in Module 05.

### Error Handling Pattern for Routes

Database calls can fail (invalid id format, connection hiccup, validation error). Every async route handler in this module follows this exact pattern:

```javascript
app.get("/api/tasks", async (req, res) => {
  try {
    const tasks = await Task.find();
    res.json(tasks);
  } catch (error) {
    res.status(500).json({ error: "Something went wrong" });
  }
});
```

`try`/`catch` lets you attempt risky code and gracefully handle failure instead of crashing the whole server.

---

## Step-by-Step Hands-On

### Step 1: Create the Task Model

1. Create a new folder at the project root: `models`
2. Create `models/Task.js` with this exact content:

```javascript
// models/Task.js

const mongoose = require("mongoose");

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
  }
}, {
  timestamps: true // automatically adds createdAt and updatedAt fields
});

const Task = mongoose.model("Task", taskSchema);

module.exports = Task;
```

`trim: true` automatically removes leading/trailing whitespace from submitted titles. `timestamps: true` is a convenience option that auto-manages `createdAt`/`updatedAt` for you.

### Step 2: Replace the In-Memory Routes in `server.js`

Open `server.js`. You will replace the entire fake-data section and all five routes from Module 08.

1. Add this near your other `require` statements at the top:
   ```javascript
   const Task = require("./models/Task");
   ```

2. **Delete** these lines entirely (the in-memory data from Module 08):
   ```javascript
   let tasks = [
     { id: 1, title: "Buy milk", description: "2% milk", completed: false },
     { id: 2, title: "Walk dog", description: "Around the block", completed: true }
   ];
   let nextId = 3;
   ```

3. **Replace** all five existing route handlers (`GET /api/tasks`, `GET /api/tasks/:id`, `POST /api/tasks`, `PUT /api/tasks/:id`, `DELETE /api/tasks/:id`) with this exact block:

```javascript
// GET /api/tasks - return all tasks from the database
app.get("/api/tasks", async (req, res) => {
  try {
    const tasks = await Task.find().sort({ createdAt: -1 }); // newest first
    res.json(tasks);
  } catch (error) {
    res.status(500).json({ error: "Failed to fetch tasks" });
  }
});

// GET /api/tasks/:id - return one task by its MongoDB _id
app.get("/api/tasks/:id", async (req, res) => {
  try {
    const task = await Task.findById(req.params.id);
    if (!task) {
      return res.status(404).json({ error: "Task not found" });
    }
    res.json(task);
  } catch (error) {
    res.status(400).json({ error: "Invalid task id" });
  }
});

// POST /api/tasks - create a new task
app.post("/api/tasks", async (req, res) => {
  try {
    const { title, description } = req.body;
    if (!title) {
      return res.status(400).json({ error: "Title is required" });
    }
    const newTask = await Task.create({ title, description });
    res.status(201).json(newTask);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// PUT /api/tasks/:id - update an existing task
app.put("/api/tasks/:id", async (req, res) => {
  try {
    const updatedTask = await Task.findByIdAndUpdate(
      req.params.id,
      req.body,
      { new: true, runValidators: true } // return the UPDATED doc, and re-check schema rules
    );
    if (!updatedTask) {
      return res.status(404).json({ error: "Task not found" });
    }
    res.json(updatedTask);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// DELETE /api/tasks/:id - remove a task
app.delete("/api/tasks/:id", async (req, res) => {
  try {
    const deletedTask = await Task.findByIdAndDelete(req.params.id);
    if (!deletedTask) {
      return res.status(404).json({ error: "Task not found" });
    }
    res.status(204).send();
  } catch (error) {
    res.status(400).json({ error: "Invalid task id" });
  }
});
```

4. Save `server.js`. If `nodemon` is running (`npm run dev`), it auto-restarts; otherwise run `npm run dev` now.

### Step 3: Test the Full Cycle

1. Using Thunder Client, send `POST` to `http://localhost:3000/api/tasks` with body:
   ```json
   { "title": "Buy milk", "description": "2% milk" }
   ```
   Confirm status `201`, and note the returned `_id` — copy it for the next steps. Notice it also now includes `createdAt` and `updatedAt`.
2. Send `GET` to `http://localhost:3000/api/tasks` — confirm your new task appears.
3. Send `GET` to `http://localhost:3000/api/tasks/<paste the _id here>` — confirm it returns that one task.
4. Send `PUT` to the same URL with body `{ "completed": true }` — confirm the response shows `"completed": true`.
5. **The critical test:** stop the server entirely (`Ctrl+C` in the terminal), then start it again (`npm run dev`). Send `GET /api/tasks` again. **Your task is still there.** This is the entire point of Module 09–10: data now survives a restart, unlike Module 08.
6. Open `mongosh`, run `use taskflow_db`, then `db.tasks.find().pretty()` — confirm you can see the exact same data from the database side too. (Or open MongoDB Compass, connect to `mongodb://localhost:27017`, and browse to `taskflow_db` → `tasks` visually.)
7. Send `DELETE` to `/api/tasks/<id>` and confirm with `GET` that it's gone for good (check again after a restart, to be thorough).

---

## Practice Exercises

1. Add a new field `priority` to `taskSchema` with `type: String`, allowed values restricted via Mongoose's `enum` option to `["low", "medium", "high"]`, and a `default: "medium"`. (Look up Mongoose's `enum` schema option — reading library documentation is a core real-world skill.) Restart the server, create a new task without specifying priority, and confirm via `GET` that it defaulted correctly.
2. Try creating a task via Thunder Client with **no title at all** (empty JSON body `{}`) and confirm you correctly get a `400` error rather than a server crash.
3. Try sending `GET /api/tasks/not-a-real-id` (an obviously invalid id, not a real MongoDB id format) and confirm the `catch` block correctly returns `400` instead of crashing the server. Check your terminal — the server should still be running afterward.

---

## Quiz

1. What is the difference between a Mongoose Schema and a Mongoose Model?
2. What does `{ new: true }` do when passed to `findByIdAndUpdate`?
3. Why do we wrap database calls in `try`/`catch`?
4. What does `timestamps: true` automatically add to documents?
5. What proved, in Step 3, that data is now permanently stored rather than temporary?
6. What status code should be returned when a client tries to create a task with no title, and why is that different from `500`?
7. What does `Task.find().sort({ createdAt: -1 })` do?

---

## Module Checklist

- [ ] `models/Task.js` exists with a schema including `title`, `description`, `completed`, and timestamps.
- [ ] All 5 routes in `server.js` now use the `Task` model instead of an in-memory array.
- [ ] I created, read, updated, and deleted a task entirely through the real API.
- [ ] I restarted the server and confirmed data persisted.
- [ ] I viewed the data directly in `mongosh` or Compass.
- [ ] I completed all 3 practice exercises, including the two intentional-error tests.

---

## What's Next

Open `11-frontend-api-integration.md` — you'll connect the `app.js` frontend code from Module 05 to this real API, so the dashboard page becomes a fully working, dynamic interface instead of a static mockup.

---

## Quiz Answer Key

1. A Schema defines the shape/rules of documents (fields, types, defaults, validation); a Model is the compiled, usable object built from a schema that you actually call methods on (`find`, `create`, etc.) to interact with the database.
2. It tells Mongoose to return the document **after** the update has been applied, rather than its original state before the update.
3. So that a failure during a database operation (bad id, validation error, connection issue) is caught and handled gracefully with an appropriate error response, instead of crashing the entire server process.
4. `createdAt` and `updatedAt` fields, automatically managed by Mongoose.
5. Stopping and restarting the server (`Ctrl+C` then `npm run dev` again) and confirming the previously created task was still returned by `GET /api/tasks` afterward.
6. `400 Bad Request`, because the failure is caused by the client sending invalid/incomplete data — not by something broken on the server, which is what `500` would imply.
7. It retrieves all tasks and sorts them by their `createdAt` field in descending order (`-1`), so the newest tasks appear first.
