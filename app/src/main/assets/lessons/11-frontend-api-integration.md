# Module 11: Frontend ↔ API Integration

## Learning Objectives

By the end of this module you will be able to:
- Fetch real data from your own API and render it dynamically into the DOM.
- Use event delegation to handle clicks/changes on elements that don't exist yet at page load.
- Wire up full Create, Read, Update, and Delete functionality from the browser UI.
- Understand a basic security risk (HTML injection) and how to guard against it.

## Estimated Time
4–6 hours.

---

## Concepts

### The Goal of This Module

Right now: `dashboard.html` has a hardcoded, empty `<ul id="task-list">`, and `app.js` (from Module 05) only shows alerts as placeholders. By the end of this module, the page will load real tasks from MongoDB (via your Module 10 API) the instant it opens, and every action (add, complete, delete) will talk to the real server with no page reloads.

### Rendering Data as HTML

Given an array of task objects from the API, you need to turn each one into a visual `<li>` element. Two common approaches:
1. Build an HTML string with template literals and insert it via `innerHTML`.
2. Build elements one-by-one with `document.createElement` and `.appendChild`.

We use a hybrid: `createElement` for the outer `<li>` (so we can easily attach data to it), and a template literal for its inner contents (faster to write/read for a moderately complex chunk of markup).

### Event Delegation — Why It's Necessary Here

In Module 05, you attached a listener directly to a button that already existed in the HTML when the page loaded. But task `<li>` elements are created **after** the page loads, every time `loadTasks()` runs — a listener attached directly to them would only ever apply to elements that existed at that exact moment, missing every future one.

The fix: attach **one** listener to the parent container (`#task-list`, which always exists), and let clicks "bubble up" to it from any child, present or future. Inside the listener, check what was actually clicked:

```javascript
taskList.addEventListener("click", (event) => {
  if (event.target.classList.contains("delete-button")) {
    // a delete button, somewhere inside taskList, was clicked
  }
});
```

`event.target` is the exact element that was clicked. `element.closest(selector)` walks upward from that element through its ancestors until it finds one matching `selector` — useful for finding "which task row is this click inside?" regardless of exactly which sub-element was clicked.

### Storing Data on Elements: `dataset`

We need to know *which* task's MongoDB `_id` corresponds to a given `<li>` when the user clicks delete on it. The `data-*` attribute convention, accessed via JavaScript's `.dataset`, is built exactly for this:

```javascript
li.dataset.id = task._id;     // sets attribute data-id="..." on the element
// later:
const id = li.dataset.id;     // reads it back
```

### A Real Security Concept: HTML Injection

If a task title contained actual HTML, like `<img src=x onerror="alert('hacked')">`, and you inserted it directly via `innerHTML` without precaution, that code could execute in the browser. This class of vulnerability is called **Cross-Site Scripting (XSS)**. The fix used in this module is a small `escapeHtml()` helper that converts any HTML-special characters in user-provided text into harmless, literal text before display — guaranteeing task titles are always shown as plain text, never executed as markup.

---

## Step-by-Step Hands-On

### Step 1: Replace the Entire Contents of `public/app.js`

Open `public/app.js`, select all existing content, delete it, and paste this exact new content:

```javascript
// app.js - TaskFlow frontend logic

console.log("app.js loaded successfully.");

const API_BASE = "/api/tasks";

// ---------------- Dashboard: Task List ----------------

const taskForm = document.getElementById("task-form");
const taskList = document.getElementById("task-list");

// Fetch all tasks from the server and render them
async function loadTasks() {
  try {
    const response = await fetch(API_BASE);
    if (!response.ok) {
      throw new Error("Failed to load tasks");
    }
    const tasks = await response.json();
    renderTasks(tasks);
  } catch (error) {
    console.error(error);
  }
}

// Build the <li> elements for each task and insert them into the page
function renderTasks(tasks) {
  taskList.innerHTML = ""; // clear current list before re-drawing

  if (tasks.length === 0) {
    taskList.innerHTML = "<li>No tasks yet. Add one above!</li>";
    return;
  }

  for (const task of tasks) {
    const li = document.createElement("li");
    li.className = "task-item" + (task.completed ? " completed" : "");
    li.dataset.id = task._id; // store the MongoDB _id directly on the element

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

// Converts text to a safe, literal string before inserting via innerHTML,
// preventing HTML/JS injection from task titles or descriptions.
function escapeHtml(text) {
  const div = document.createElement("div");
  div.textContent = text;
  return div.innerHTML;
}

// Handle adding a new task
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

      if (!response.ok) {
        throw new Error("Failed to create task");
      }

      titleInput.value = "";
      descriptionInput.value = "";
      loadTasks(); // refresh the list so the new task appears
    } catch (error) {
      console.error(error);
      alert("Could not add task. Please try again.");
    }
  });
}

// Event delegation: ONE listener on the parent list handles clicks
// on delete buttons inside ANY task row, including ones added later.
if (taskList) {
  taskList.addEventListener("click", async (event) => {
    const li = event.target.closest(".task-item");
    if (!li) return;
    const id = li.dataset.id;

    if (event.target.classList.contains("delete-button")) {
      try {
        const response = await fetch(`${API_BASE}/${id}`, { method: "DELETE" });
        if (!response.ok && response.status !== 204) {
          throw new Error("Failed to delete task");
        }
        loadTasks();
      } catch (error) {
        console.error(error);
        alert("Could not delete task.");
      }
    }
  });

  // Same pattern, but for the "change" event, to catch checkbox toggles
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
      if (!response.ok) {
        throw new Error("Failed to update task");
      }
      loadTasks();
    } catch (error) {
      console.error(error);
      alert("Could not update task.");
    }
  });
}

// Load tasks immediately when the dashboard page opens
if (taskList) {
  loadTasks();
}

// ---------------- Auth pages (login/register) ----------------
// Placeholder only for now — fully implemented in Module 12.
const logoutButton = document.getElementById("logout-button");
if (logoutButton) {
  logoutButton.addEventListener("click", () => {
    console.log("Logout clicked — will be fully wired up in Module 12.");
  });
}
```

### Step 2: Update CSS Slightly for the Checkbox Layout

Open `public/style.css` and add this new rule at the end of the file (this makes the checkbox/label/text line up cleanly):

```css
.task-item label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: normal;
  cursor: pointer;
}
```

### Step 3: Test the Full Loop

1. Make sure your server is running: `npm run dev`.
2. Open `http://localhost:3000/dashboard.html` in your browser.
3. Open Developer Tools → Console. Confirm `"app.js loaded successfully."` appears, and no red errors.
4. **If you have leftover test tasks from earlier modules**, they should now appear in the list automatically, fetched live from MongoDB.
5. Add a new task using the form. Confirm: no page reload, no alert (we removed it), and the new task appears in the list within a second.
6. Click a task's checkbox. Confirm the `<li>` visually updates (struck-through, faded — from the `.completed` CSS class you wrote in Module 04) and stays that way after a manual page refresh.
7. Click **Delete** on a task. Confirm it disappears and does not return after a manual page refresh.
8. Open the **Network** tab in Developer Tools, repeat one action (e.g., add a task), and find the actual `POST` request — confirm its status is `201` and its **Request payload**/Response match what you expect.

---

## Practice Exercises

1. Add an "Edit" button next to "Delete" on each task. When clicked, use `prompt("New title:", task's current title)` (a simple built-in browser dialog) to get new text, then send a `PUT` request updating the title, then call `loadTasks()` again. (Hint: you'll need the task's current title available — consider storing it in another `data-*` attribute, e.g. `li.dataset.title = task.title`.)
2. Add a small task counter above the list, e.g. `"3 tasks, 1 completed"`, computed in JavaScript from the array returned by `loadTasks()` and inserted into a `<p id="task-summary"></p>` you add to `dashboard.html`.
3. Intentionally type a task title like `<b>test</b>` and confirm it displays as the literal text `<b>test</b>` on the page (proving `escapeHtml` is working), rather than rendering as bold text.

---

## Quiz

1. Why can't you attach a click listener directly to each task's delete button at page-load time?
2. What does `element.closest(".task-item")` do?
3. What is stored in `li.dataset.id`, and why do we need it?
4. What security risk does `escapeHtml()` protect against?
5. After deleting a task, why do we call `loadTasks()` again instead of just removing the `<li>` from the page directly with JavaScript?
6. What does `response.ok` tell you about a fetch response?
7. What event name do we listen for to detect a checkbox being toggled?

---

## Module Checklist

- [ ] `app.js` was fully replaced with the new version above.
- [ ] Opening `dashboard.html` loads real tasks from MongoDB automatically.
- [ ] Adding, completing, and deleting tasks all work with zero page reloads.
- [ ] Changes survive a manual browser refresh (proof they're really hitting the database).
- [ ] I completed all 3 practice exercises.

---

## What's Next

Open `12-authentication.md` — right now, anyone can see and modify any task with no login at all. You'll add real user accounts so each person only sees their own tasks.

---

## Quiz Answer Key

1. Those buttons don't exist yet at page-load time — they are created dynamically by `renderTasks()` after data is fetched, so a listener attached only at load time would never apply to them (or to any future task added later).
2. It searches upward from the clicked element through its ancestors in the DOM tree, returning the nearest one that matches the given CSS selector — used here to find which task `<li>` a click happened inside.
3. The MongoDB `_id` of that task, stored so that later actions (delete, update) know exactly which database document to target, without needing to re-fetch or guess it.
4. Cross-Site Scripting (XSS) / HTML injection — where malicious HTML or script embedded in user-provided text could otherwise execute in the browser if inserted unescaped via `innerHTML`.
5. Calling `loadTasks()` re-fetches the current, true state directly from the database and re-renders accordingly, guaranteeing the UI always reflects reality — rather than trusting that a local DOM removal alone is enough (which could drift out of sync if, say, the delete request actually failed).
6. Whether the HTTP status code was in the successful range (200–299); it's `false` for error statuses like 404 or 500, letting you branch into error-handling logic.
7. `"change"`.
