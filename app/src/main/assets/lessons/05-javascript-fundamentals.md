# Module 05: JavaScript Fundamentals

## Learning Objectives

By the end of this module you will be able to:
- Use variables, data types, operators, conditionals, and loops.
- Write and call functions, including arrow functions.
- Work with arrays and objects, including common array methods.
- Select and manipulate HTML elements from JavaScript (the DOM).
- Respond to user events like clicks.
- Understand the basics of asynchronous code and `fetch`, in preparation for Module 11.

## Estimated Time
8–12 hours (this is the largest foundational module — take your time, it underpins everything after it).

---

## Concepts

### What Is JavaScript?

JavaScript is a programming language. Unlike HTML/CSS (structure/style), JavaScript adds **behavior and logic**: responding to clicks, calculating things, fetching data, updating the page without reloading. It is the only language that runs natively in every browser, and — via Node.js — is also what runs your backend server in this course. Learning it once lets you write both halves of the stack.

### Variables

```javascript
let age = 25;        // can be reassigned later
const name = "Sam";  // cannot be reassigned
```

Use `const` by default; use `let` only when you know the value must change later. Avoid the old `var` keyword — it has confusing scoping rules that `let`/`const` fixed.

### Data Types

```javascript
let aString = "hello";     // text, in quotes
let aNumber = 42;          // numbers (no separate "integer" type)
let aBoolean = true;       // true or false
let nothingYet = null;     // intentionally empty
let notDefined;            // undefined — declared but no value yet
let aList = [1, 2, 3];     // array
let anObject = { key: "value" }; // object
```

Check a value's type with `typeof aNumber` → `"number"`.

### Operators

```javascript
5 + 3      // 8   addition
5 - 3      // 2
5 * 3      // 15
5 / 3      // 1.666...
5 % 3      // 2   remainder ("modulo")

5 === 5    // true   strict equality (always prefer this)
5 === "5"  // false  different types, so not equal
5 == "5"   // true   loose equality — AVOID, it "converts" types unpredictably

5 > 3      // true
5 >= 5     // true

true && false   // false  (AND — both must be true)
true || false   // true   (OR — at least one true)
!true           // false  (NOT — flips it)
```

**Always use `===` and `!==`**, never `==`/`!=`, unless you have a specific reason — this avoids a whole category of confusing bugs.

### Template Literals (String Building)

```javascript
const name = "Sam";
const greeting = `Hello, ${name}!`; // backticks, ${} inserts a variable
console.log(greeting); // Hello, Sam!
```
Prefer this over `"Hello, " + name + "!"` — it's more readable, especially with multiple variables.

### Conditionals

```javascript
const score = 72;

if (score >= 90) {
  console.log("A");
} else if (score >= 70) {
  console.log("B");
} else {
  console.log("Needs improvement");
}
```

### Loops

```javascript
// Classic for loop
for (let i = 0; i < 5; i++) {
  console.log(i); // 0, 1, 2, 3, 4
}

// for...of - iterate over array values directly (most common in this course)
const tasks = ["Buy milk", "Walk dog"];
for (const task of tasks) {
  console.log(task);
}

// while loop
let count = 0;
while (count < 3) {
  console.log(count);
  count++;
}
```

### Functions

```javascript
// Function declaration
function add(a, b) {
  return a + b;
}

// Arrow function (common modern style, used heavily in this course)
const add2 = (a, b) => {
  return a + b;
};

// Arrow function shorthand (single expression, implicit return)
const add3 = (a, b) => a + b;

console.log(add(2, 3)); // 5
```

Functions let you package logic once and reuse it by calling its name with different inputs ("arguments").

### Arrays and Common Array Methods

```javascript
const tasks = ["Buy milk", "Walk dog", "Read book"];

tasks.length          // 3
tasks[0]               // "Buy milk" (arrays are zero-indexed)
tasks.push("New task") // adds to the end
tasks.includes("Walk dog") // true

// .map() - transform every item, returns a NEW array
const shouted = tasks.map((task) => task.toUpperCase());
// ["BUY MILK", "WALK DOG", "READ BOOK"]

// .filter() - keep only items that pass a test, returns a NEW array
const longTasks = tasks.filter((task) => task.length > 8);

// .find() - return the first item that matches, or undefined
const found = tasks.find((task) => task === "Walk dog");

// .forEach() - run code for every item, returns nothing
tasks.forEach((task) => console.log(task));
```

`.map()` and `.filter()` are used **constantly** once you're rendering lists of tasks from data — make sure these feel comfortable before moving on.

### Objects

```javascript
const task = {
  title: "Buy milk",
  completed: false,
  priority: "medium"
};

task.title          // "Buy milk"  (dot notation)
task["title"]       // "Buy milk"  (bracket notation - useful when the key is a variable)
task.completed = true; // update a property

// Destructuring - pull properties out into their own variables
const { title, completed } = task;
console.log(title); // "Buy milk"
```

Recall from Module 01: this object shape is exactly what JSON looks like — that consistency is intentional and will make Module 9–11 feel natural.

### Arrays of Objects (exactly how TaskFlow's data will look)

```javascript
const allTasks = [
  { title: "Buy milk", completed: false },
  { title: "Walk dog", completed: true }
];

const incomplete = allTasks.filter((t) => !t.completed);
```

### The DOM (Document Object Model)

The DOM is JavaScript's live, in-memory representation of the HTML page — letting you read and change the page after it has loaded.

```javascript
// Selecting elements
const heading = document.querySelector("h1");        // first match
const allButtons = document.querySelectorAll("button"); // all matches (a list)
const taskList = document.getElementById("task-list");  // by id

// Reading/changing content
heading.textContent = "New Heading Text";
heading.innerHTML = "<em>Italic Heading</em>"; // careful: only use with trusted content

// Changing styles/classes
heading.style.color = "blue";
heading.classList.add("highlight");
heading.classList.remove("highlight");
heading.classList.toggle("highlight"); // adds if missing, removes if present

// Creating new elements
const newItem = document.createElement("li");
newItem.textContent = "A new task";
taskList.appendChild(newItem);
```

### Events

```javascript
const button = document.querySelector("#logout-button");

button.addEventListener("click", () => {
  console.log("Button was clicked!");
});
```

`addEventListener` takes an event name (`"click"`, `"submit"`, `"input"`, `"keydown"`, etc.) and a function to run when that event fires.

For forms specifically, you almost always want:

```javascript
const form = document.querySelector("#task-form");

form.addEventListener("submit", (event) => {
  event.preventDefault(); // stop the default full-page reload/navigation
  console.log("Form submitted without reloading the page!");
});
```

`event.preventDefault()` is essential — without it, submitting a form reloads the whole page, which we never want in a dynamic app.

### Asynchronous JavaScript (Preview — used fully in Module 11)

Some operations (like asking a server for data) take time and don't finish instantly. JavaScript handles this with **Promises** and the `async`/`await` keywords:

```javascript
async function loadTasks() {
  const response = await fetch("/api/tasks"); // wait for the network request
  const data = await response.json();          // wait for the body to parse as JSON
  console.log(data);
}
```

- `fetch(url)` sends an HTTP request (defaults to `GET`) and returns a Promise.
- `await` pauses the function (without freezing the whole page) until that Promise resolves.
- A function containing `await` must be marked `async`.

You don't need to fully master this yet — just recognize the shape. Module 11 will use it heavily, step by step.

---

## Step-by-Step Hands-On

### Step 1: Create `public/app.js`

1. Create file `public/app.js` (it's already linked via `<script src="app.js">` in both HTML files from Module 03).
2. Paste this exact content:

```javascript
// app.js - JavaScript fundamentals practice, wired into the real TaskFlow pages.

console.log("app.js loaded successfully.");

// --- Practice 1: variables, template literals ---
const appName = "TaskFlow";
const version = 1;
console.log(`Welcome to ${appName}, version ${version}.`);

// --- Practice 2: a simple function ---
function describeTaskCount(count) {
  if (count === 0) {
    return "You have no tasks yet.";
  } else if (count === 1) {
    return "You have 1 task.";
  } else {
    return `You have ${count} tasks.`;
  }
}
console.log(describeTaskCount(0));
console.log(describeTaskCount(1));
console.log(describeTaskCount(5));

// --- Practice 3: arrays and array methods ---
const sampleTasks = [
  { title: "Buy milk", completed: false },
  { title: "Walk dog", completed: true },
  { title: "Read book", completed: false }
];

const incompleteTasks = sampleTasks.filter((task) => !task.completed);
console.log("Incomplete tasks:", incompleteTasks);

const titlesOnly = sampleTasks.map((task) => task.title);
console.log("Titles only:", titlesOnly);

// --- Practice 4: DOM selection + event listener ---
// This only runs real effects on dashboard.html, since that's where
// #logout-button and #task-form exist. On index.html these will safely do nothing.

const logoutButton = document.getElementById("logout-button");
if (logoutButton) {
  logoutButton.addEventListener("click", () => {
    alert("Logout clicked! We'll wire this up for real in Module 12.");
  });
}

const taskForm = document.getElementById("task-form");
if (taskForm) {
  taskForm.addEventListener("submit", (event) => {
    event.preventDefault(); // stop the page from reloading
    const titleInput = document.getElementById("task-title");
    console.log(`Form submitted with title: "${titleInput.value}"`);
    alert(`(Practice only) You typed: ${titleInput.value}`);
  });
}
```

### Step 2: Run It and Watch the Console

1. Open `dashboard.html` in your browser (via Live Server or by double-clicking the file).
2. Open Developer Tools (`F12`) and click the **Console** tab.
3. You should see the `console.log` output from Practices 1–3 printed there.
4. In the page itself, type a task title into the "Add a New Task" form and click **Add Task**. Confirm:
   - The page does **not** reload.
   - An alert pops up showing what you typed.
   - The console shows the matching log line.
5. Click **Log Out**. Confirm an alert appears.
6. Open `index.html` as well and confirm the console still shows Practices 1–3 logging (proving the same file runs on both pages), with no errors about missing elements (because of the `if (logoutButton)` safety checks).

---

## Practice Exercises

1. Write a function `isLongTask(title)` that returns `true` if the title is longer than 20 characters, otherwise `false`. Call it with two different example strings and `console.log` the results.
2. Using `sampleTasks` from `app.js`, use `.find()` to locate the task titled `"Walk dog"` and log it.
3. Add a new `<button id="practice-button">Click Me</button>` anywhere inside `dashboard.html`, then in `app.js` select it and add a click listener that toggles a `highlight` CSS class on it (add a `.highlight { background-color: yellow; }` rule to `style.css` to see the effect).

---

## Quiz

1. What is the difference between `let` and `const`?
2. Why should you use `===` instead of `==`?
3. What does `.map()` return, and how is that different from `.forEach()`?
4. What does `event.preventDefault()` do inside a form's submit handler, and why do we need it?
5. What is the DOM?
6. Write the syntax for an arrow function called `square` that takes one number and returns its square.
7. What keyword must a function have if it uses `await` inside it?
8. What does `fetch()` return?

---

## Module Checklist

- [ ] I created `public/app.js` with all four practice blocks.
- [ ] I confirmed console output appears correctly in both `index.html` and `dashboard.html`.
- [ ] Submitting the task form does NOT reload the page.
- [ ] I completed all 3 practice exercises.
- [ ] I'm comfortable with `.map()` and `.filter()` — re-read that section if not, it's used constantly later.

---

## What's Next

Open `06-git-version-control.md` to start saving checkpoints of your TaskFlow project so you can never lose work or be afraid to experiment.

---

## Quiz Answer Key

1. `let` declares a variable that can be reassigned later; `const` declares one that cannot be reassigned after its initial value (though if it's an object/array, its contents can still be mutated).
2. `===` (strict equality) compares both value and type without converting types, avoiding unpredictable results that `==` (loose equality) can produce (e.g., `0 == false` is `true`).
3. `.map()` returns a brand new array of the same length, with each item transformed by the callback; `.forEach()` returns nothing (`undefined`) and is used purely to run side effects (like logging) for each item.
4. It stops the browser's default behavior of reloading/navigating when a form is submitted, so your JavaScript can handle the submission instead (e.g., sending data via `fetch` without a full page reload).
5. The Document Object Model — JavaScript's live, in-memory representation of the HTML page, which can be read and modified after the page has loaded.
6. `const square = (n) => n * n;`
7. `async`.
8. A Promise, which eventually resolves to a `Response` object representing the HTTP response.
