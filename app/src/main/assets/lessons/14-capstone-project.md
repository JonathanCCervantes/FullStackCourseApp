# Module 14: Capstone — Finish & Extend TaskFlow

## Learning Objectives

By the end of this module you will:
- Have a complete mental map of every file in your finished project and what it does.
- Have implemented full task-editing and task-priority features end-to-end.
- Have independently extended the app with at least one self-directed feature.
- Have run a full, structured QA pass across the entire application.

## Estimated Time
6–10 hours.

---

## Your Final Project Structure

Before adding anything new, confirm your `taskflow` folder matches this exactly (folders marked with `/`):

```
taskflow/
├── .env                        (NOT committed to Git)
├── .gitignore
├── package.json
├── package-lock.json
├── server.js
├── playground.js               (optional leftover from Module 07 — safe to delete now)
├── math-helpers.js             (optional leftover from Module 07 — safe to delete now)
├── config/
│   └── db.js
├── middleware/
│   └── requireAuth.js
├── models/
│   ├── Task.js
│   └── User.js
├── routes/
│   ├── authRoutes.js
│   └── taskRoutes.js
├── utils/
│   └── validators.js
├── tests/
│   └── validators.test.js
├── public/
│   ├── index.html
│   ├── dashboard.html
│   ├── style.css
│   └── app.js
└── node_modules/               (NOT committed to Git — ignored)
```

If anything is missing or named differently, go back to the relevant module before continuing — this module assumes everything above already works.

If you still have `playground.js` and `math-helpers.js` from Module 07's pure-Node practice, you can safely delete them now (`git rm playground.js math-helpers.js` then commit), since they were never part of the real app — or keep them; they don't interfere with anything.

---

## Feature 1: Full Task Editing (Completing the Module 11 Hint)

### Backend

No backend changes needed — `PUT /api/tasks/:id` already accepts any combination of `title`, `description`, and `completed` from Module 10/12.

### Frontend: `public/dashboard.html`

Add an Edit button next to Delete by changing the task form section — actually, the edit button is generated dynamically in JavaScript, so no HTML file changes are needed here.

### Frontend: `public/app.js`

In `renderTasks()`, update the `li.innerHTML` template to add an Edit button and store the current title/description as extra data attributes:

Replace:
```javascript
    li.innerHTML = `
      <label>
        <input type="checkbox" class="task-checkbox" ${task.completed ? "checked" : ""}>
        <strong>${escapeHtml(task.title)}</strong>
        ${task.description ? `- ${escapeHtml(task.description)}` : ""}
      </label>
      <button class="delete-button" type="button">Delete</button>
    `;
```
with:
```javascript
    li.dataset.title = task.title;
    li.dataset.description = task.description || "";

    li.innerHTML = `
      <label>
        <input type="checkbox" class="task-checkbox" ${task.completed ? "checked" : ""}>
        <strong>${escapeHtml(task.title)}</strong>
        ${task.description ? `- ${escapeHtml(task.description)}` : ""}
      </label>
      <span class="task-actions">
        <button class="edit-button" type="button">Edit</button>
        <button class="delete-button" type="button">Delete</button>
      </span>
    `;
```

Then, inside the existing `taskList.addEventListener("click", ...)` handler, add a new `else if` branch (alongside the existing delete-button check):

```javascript
    } else if (event.target.classList.contains("edit-button")) {
      const currentTitle = li.dataset.title;
      const currentDescription = li.dataset.description;

      const newTitle = prompt("Edit title:", currentTitle);
      if (newTitle === null || newTitle.trim() === "") return; // cancelled or emptied

      const newDescription = prompt("Edit description:", currentDescription);
      if (newDescription === null) return; // cancelled

      try {
        const response = await fetch(`${API_BASE}/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ title: newTitle.trim(), description: newDescription.trim() })
        });
        if (!response.ok) throw new Error("Failed to update task");
        loadTasks();
      } catch (error) {
        console.error(error);
        alert("Could not update task.");
      }
    }
```

Add a small CSS rule to `public/style.css` for the new button grouping:
```css
.task-actions {
  display: flex;
  gap: 8px;
}
```

**Test it:** click Edit on any task, change the title via the prompt dialog, confirm it updates in the list and survives a refresh.

---

## Feature 2: Task Priority

### Backend: `models/Task.js`

Add this field to the schema (alongside the existing ones):

```javascript
  priority: {
    type: String,
    enum: ["low", "medium", "high"],
    default: "medium"
  },
```

### Frontend: `public/dashboard.html`

In the `#task-form`, add a dropdown right before the submit button:

```html
        <label for="task-priority">Priority</label>
        <select id="task-priority" name="priority">
          <option value="low">Low</option>
          <option value="medium" selected>Medium</option>
          <option value="high">High</option>
        </select>
```

### Frontend: `public/app.js`

In the task-form submit handler, read the new field and include it in the request body. Change:
```javascript
    const title = titleInput.value.trim();
    const description = descriptionInput.value.trim();
    if (!title) return;

    try {
      const response = await fetch(API_BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, description })
      });
```
to:
```javascript
    const title = titleInput.value.trim();
    const description = descriptionInput.value.trim();
    const priority = document.getElementById("task-priority").value;
    if (!title) return;

    try {
      const response = await fetch(API_BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, description, priority })
      });
```

In `renderTasks()`, add a priority class to each `<li>` so we can color-code it. Change:
```javascript
    li.className = "task-item" + (task.completed ? " completed" : "");
```
to:
```javascript
    li.className = `task-item priority-${task.priority}` + (task.completed ? " completed" : "");
```

### CSS: `public/style.css`

Add these three rules:
```css
.task-item.priority-high {
  border-left: 4px solid #dc2626;
}
.task-item.priority-medium {
  border-left: 4px solid #f59e0b;
}
.task-item.priority-low {
  border-left: 4px solid #10b981;
}
```

**Test it:** create one task of each priority and confirm the colored left border matches.

---

## Stretch Challenges (Your Turn — No Full Solutions Given)

You have every skill needed for these. Hints are given; the implementation is yours, which is exactly how independent development feels.

### Challenge A: Due Dates
- Add a `dueDate` field to the Task schema (`type: Date`).
- Add a `<input type="date">` to the task form.
- Display the due date on each task, and add a CSS class (e.g., `.overdue`) when `dueDate` is in the past and the task isn't completed — you'll need to compare `new Date(task.dueDate)` against `new Date()` in `app.js`.

### Challenge B: Search / Filter
- Add a text `<input>` above the task list for live search.
- On the `"input"` event of that field, filter the already-loaded tasks array by whether their title includes the typed text (case-insensitive — look up `.toLowerCase()`), and call `renderTasks()` with just the filtered subset, without re-fetching from the server.

### Challenge C: Task Counts by Priority
- Above the task list, show three small counts: "2 High · 3 Medium · 1 Low," computed from the current tasks array using `.filter()`, exactly the way you used `.filter()` back in Module 05.

---

## Full Application End-to-End QA Pass

Run through this entire list in order, on a freshly restarted server, before considering the project "done." This is exactly the kind of structured pass a professional would do before considering a feature complete.

- [ ] Fresh `npm run dev` start shows both "MongoDB connected successfully" and "TaskFlow server running."
- [ ] Visiting `/dashboard.html` while logged out redirects to `/index.html`.
- [ ] Registering with a username under 3 characters is rejected with a clear message.
- [ ] Registering with a password under 6 characters is rejected with a clear message.
- [ ] Registering with a duplicate username is rejected with a clear message (`409`).
- [ ] Successful registration logs the user in immediately and lands on the dashboard.
- [ ] Logging out and visiting `/dashboard.html` again redirects to login.
- [ ] Logging in with a wrong password is rejected with a generic, non-revealing message.
- [ ] Logging in with a nonexistent username is rejected with the same generic message.
- [ ] Adding a task with empty title is blocked (frontend `required` and/or backend `400`).
- [ ] Added tasks appear instantly without a page reload.
- [ ] Editing a task's title/description updates correctly and persists after refresh.
- [ ] Toggling complete visually marks the task and persists after refresh.
- [ ] Deleting a task removes it and it does not reappear after refresh.
- [ ] Priority selection at creation time is reflected with the correct color-coded border.
- [ ] A second account sees zero tasks belonging to the first account.
- [ ] Restarting the server entirely preserves all data (users and tasks).
- [ ] `npm test` passes all automated tests.
- [ ] Browser console shows zero red errors on both pages, in a full click-through of every feature.
- [ ] `git log --oneline` shows a clean, readable history of meaningful commits across the whole build.

If anything on this list fails, that's normal and valuable — debug it using Module 13's techniques rather than skipping it.

---

## Final Cumulative Quiz (Spanning the Whole Course)

1. Trace the full journey of clicking "Add Task," from the click, through every file/layer, to the database, and back — name every file involved in order.
2. Why does the frontend never see a user's real password, at any point, even right after they typed it during registration?
3. What three things does `.gitignore` keep out of version control in this project, and why each?
4. What is the difference between `git add`, `git commit`, and (if you'd used it) `git push`?
5. Why does every task-related database query include `userId: req.session.userId`?
6. What would happen, functionally, if you removed `app.use(express.json())` from `server.js`?
7. Why is `escapeHtml()` necessary even though all task data ultimately comes from your own database that you control?
8. What's the functional difference between `git branch` and `git switch`?
9. Why did Module 10 wrap every database call in `try`/`catch`?
10. What does `npm run dev` do differently from `npm start`, and why do we prefer the former while building?

---

## Module Checklist

- [ ] Full task editing is implemented and tested.
- [ ] Task priority is implemented, including color-coded UI, and tested.
- [ ] I attempted at least one of the three stretch challenges independently.
- [ ] I completed the full End-to-End QA Pass and fixed anything that failed.
- [ ] I attempted the Final Cumulative Quiz before checking the answer key.

---

## What's Next

Open `15-next-steps-and-deployment.md` for where to go from here — including an optional, clearly-marked appendix on cloud deployment (which requires creating a free account with a hosting provider, unlike everything else in this course).

---

## Final Cumulative Quiz — Answer Key

1. User clicks "Add Task" in the browser → `public/app.js`'s submit handler intercepts it, builds JSON, sends a `fetch POST` to `/api/tasks` → `server.js`'s middleware chain (logging, session, JSON parsing) → routed to `routes/taskRoutes.js`'s `POST /` handler → `models/Task.js`'s `Task.create()` → Mongoose sends the write to MongoDB via the connection established in `config/db.js` → MongoDB confirms → the new task JSON flows back through the same chain → `app.js` calls `loadTasks()` to re-fetch and `renderTasks()` to redraw the list.
2. The frontend sends the plain password only once, over the request to `/api/auth/register`; the backend immediately hashes it with `bcrypt.hash()` before storing anything, and only the irreversible hash is ever saved to `models/User.js`'s `password` field — the plain text is never written to the database or logged.
3. `node_modules/` (huge, reproducible from `package.json`, not authored by you); `.env` (secrets/configuration like `MONGODB_URI` and `SESSION_SECRET` that must never be shared or exposed publicly); `.DS_Store`/`*.log` (irrelevant system/log files with no project value).
4. `git add` stages specific changes for the next commit; `git commit` permanently saves the staged snapshot with a message into your local repository history; `git push` (not used in this course) would upload your local commits to a remote-hosted copy such as GitHub, which requires an account.
5. To enforce authorization — ensuring every read, update, or delete operation only ever matches documents owned by the currently logged-in user, making it impossible for one user to access another's tasks even if they know or guess a valid task id.
6. `req.body` would be `undefined` on every incoming request with a JSON payload, breaking every route that reads `req.body` (registration, login, task creation/updates) since they'd have no data to work with.
7. Because the database itself doesn't prevent a user from typing HTML-like text into a task title or description in the first place; the threat isn't "did I write malicious data," it's "did a user (possibly with bad intent) submit text containing HTML/script that could execute if rendered unescaped" — the database faithfully stores exactly what was submitted.
8. `git branch` creates a new branch (or, with no arguments, lists existing branches) but does not move you onto it; `git switch` moves your working directory onto a different, already-existing branch.
9. So that a failure during any database operation (invalid id format, validation error, connection hiccup) is caught and answered with an appropriate error response, instead of crashing the entire server process for everyone using it.
10. `npm run dev` runs the server through `nodemon`, which automatically restarts it on every file save; `npm start` runs it once via plain `node` with no auto-restart — `dev` is preferred while actively coding so changes take effect immediately without manual restarts.
