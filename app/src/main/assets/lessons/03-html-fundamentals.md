# Module 03: HTML Fundamentals

## Learning Objectives

By the end of this module you will be able to:
- Explain what HTML is and how a browser uses it.
- Write valid HTML documents using common tags, attributes, forms, and semantic elements.
- Have built the two real HTML pages that make up TaskFlow's frontend skeleton.

## Estimated Time
3–5 hours.

---

## Concepts

### What Is HTML?

**HTML** (HyperText Markup Language) is not a programming language — it's a **markup language**. It describes the *structure and content* of a page: "this is a heading," "this is a paragraph," "this is a button." It does not describe how things look (that's CSS, Module 4) or how things behave (that's JavaScript, Module 5).

### Tags, Elements, and Attributes

- A **tag** is written with angle brackets: `<p>`.
- Most tags come in pairs: an opening tag `<p>` and a closing tag `</p>`, wrapping content to form an **element**: `<p>Hello</p>`.
- Some tags are "self-closing" / "void" — they have no content and no closing tag, e.g. `<input>`, `<img>`, `<br>`.
- **Attributes** add extra information inside the opening tag: `<input type="text" placeholder="Your name">`. Here `type` and `placeholder` are attributes.

### The Skeleton of Every HTML Document

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Page Title</title>
</head>
<body>
  <!-- visible content goes here -->
</body>
</html>
```

- `<!DOCTYPE html>` — tells the browser "this is modern HTML5."
- `<html>` — wraps the entire page.
- `<head>` — metadata not shown directly on the page (title, character encoding, links to CSS files).
- `<body>` — everything visible to the user goes here.

### Common Tags You'll Use Constantly

| Tag | Purpose |
|---|---|
| `<h1>` to `<h6>` | Headings, `h1` biggest/most important |
| `<p>` | Paragraph of text |
| `<a href="...">` | Link |
| `<div>` | Generic block container (no meaning by itself, used for layout/grouping) |
| `<span>` | Generic inline container (for grouping small bits of text) |
| `<ul>` / `<ol>` / `<li>` | Unordered/ordered list, and list item |
| `<button>` | Clickable button |
| `<form>` | Wraps inputs meant to be submitted together |
| `<input>` | A field for user input (text, password, checkbox, etc., set by `type` attribute) |
| `<label>` | Text label tied to an input (improves accessibility) |
| `<img src="...">` | Image |

### Semantic HTML

"Semantic" tags describe their *meaning*, not just a generic box. They make pages more readable, accessible (e.g., for screen readers), and SEO-friendly:

| Tag | Meaning |
|---|---|
| `<header>` | Introductory content/banner for a page or section |
| `<nav>` | Navigation links |
| `<main>` | The primary content of the page |
| `<section>` | A thematic grouping of content |
| `<footer>` | Footer content |

Where a generic `<div>` would also "work" visually, prefer semantic tags when one fits — it costs nothing and adds meaning.

### Forms — Critical for TaskFlow

A `<form>` groups inputs together so they can be submitted as one unit:

```html
<form id="login-form">
  <label for="username">Username</label>
  <input type="text" id="username" name="username" required>

  <label for="password">Password</label>
  <input type="password" id="password" name="password" required>

  <button type="submit">Log In</button>
</form>
```

Key points:
- `for="username"` on the `<label>` matches `id="username"` on the `<input>` — clicking the label focuses the input.
- `type="password"` masks what's typed.
- `required` is a built-in validation attribute — the browser won't submit the form if it's empty.
- `id` uniquely identifies one element on the page (used later by JavaScript and CSS to target it precisely). `name` is the key used when form data is submitted.
- We will intercept this form with JavaScript in Module 11 rather than letting it submit the traditional way — but build it as a normal form for now.

### Comments

```html
<!-- this is a comment, ignored by the browser -->
```

---

## Step-by-Step Hands-On: Build TaskFlow's HTML

### Step 1: Create the Folder Structure

In VS Code's terminal (inside your `taskflow` folder from Module 02), run:

```
mkdir public
```

Everything the browser directly loads will live in `public/`.

### Step 2: Create `public/index.html` (Login / Register Page)

1. In VS Code, right-click the `public` folder → **New File** → name it exactly `index.html`.
2. Paste this exact content into it and save (`Ctrl+S` / `Cmd+S`):

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>TaskFlow - Log In</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <main class="auth-page">
    <h1>TaskFlow</h1>
    <p class="subtitle">Organize your tasks, simply.</p>

    <section id="login-section">
      <h2>Log In</h2>
      <form id="login-form">
        <label for="login-username">Username</label>
        <input type="text" id="login-username" name="username" required>

        <label for="login-password">Password</label>
        <input type="password" id="login-password" name="password" required>

        <button type="submit">Log In</button>
      </form>
      <p id="login-error" class="error-message"></p>
    </section>

    <section id="register-section">
      <h2>New here? Create an Account</h2>
      <form id="register-form">
        <label for="register-username">Choose a Username</label>
        <input type="text" id="register-username" name="username" required minlength="3">

        <label for="register-password">Choose a Password</label>
        <input type="password" id="register-password" name="password" required minlength="6">

        <button type="submit">Create Account</button>
      </form>
      <p id="register-error" class="error-message"></p>
    </section>
  </main>

  <script src="app.js"></script>
</body>
</html>
```

Notes on what you just typed:
- `<meta name="viewport"...>` makes the page render sensibly on mobile screens — standard on every modern page.
- `<link rel="stylesheet" href="style.css">` will connect a CSS file you create in Module 04. The page will look unstyled/plain until then — that's expected.
- `<script src="app.js">` connects a JavaScript file you create in Module 05/11. Nothing happens on click yet — that's expected.
- The empty `<p id="...-error">` elements are placeholders JavaScript will later fill with error messages (e.g., "wrong password").

### Step 3: Create `public/dashboard.html` (Task List Page)

1. Create `public/dashboard.html`.
2. Paste this exact content:

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>TaskFlow - Dashboard</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <header class="dashboard-header">
    <h1>TaskFlow</h1>
    <span id="welcome-message"></span>
    <button id="logout-button">Log Out</button>
  </header>

  <main class="dashboard-main">
    <section class="new-task-section">
      <h2>Add a New Task</h2>
      <form id="task-form">
        <label for="task-title">Title</label>
        <input type="text" id="task-title" name="title" required>

        <label for="task-description">Description</label>
        <input type="text" id="task-description" name="description">

        <button type="submit">Add Task</button>
      </form>
    </section>

    <section class="task-list-section">
      <h2>Your Tasks</h2>
      <ul id="task-list">
        <!-- Task items will be inserted here by JavaScript in Module 11 -->
      </ul>
    </section>
  </main>

  <script src="app.js"></script>
</body>
</html>
```

### Step 4: View Your Pages in the Browser

1. In VS Code's file Explorer, right-click `public/index.html`.
2. Choose **"Reveal in File Explorer"** (Windows) / **"Reveal in Finder"** (macOS), then double-click `index.html` there to open it in your default browser.
   - Alternative: in VS Code, install the free **"Live Server"** extension (Extensions icon → search "Live Server" by Ritwick Dey → Install), then right-click `index.html` in VS Code → **"Open with Live Server."** This auto-refreshes the browser whenever you save — very useful going forward.
3. You should see plain, unstyled black text and form fields: "TaskFlow," "Log In" section, "Create an Account" section. This confirms the HTML structure is correct. It will look much better after Module 04.
4. Repeat for `dashboard.html` to confirm it also loads.

---

## Practice Exercises

1. Add a `<footer>` to both pages containing a paragraph like `<p>© 2026 TaskFlow</p>`.
2. In `dashboard.html`, add a third input to the task form: a `<select>` dropdown named `priority` with three `<option>`s: `Low`, `Medium`, `High`. (Look up the `<select>`/`<option>` tag syntax — this is a normal and expected part of learning to read documentation.)
3. View the page source of any real website (right-click → "View Page Source") and find one semantic tag (`<header>`, `<nav>`, `<main>`, `<footer>`, or `<section>`) being used.

---

## Quiz

1. What is the difference between a tag and an element?
2. What goes inside `<head>` versus `<body>`?
3. What does the `for` attribute on a `<label>` do?
4. Why use `type="password"` instead of `type="text"` for a password field?
5. What is the purpose of the `required` attribute on an `<input>`?
6. Give one example of a semantic tag and what it communicates.
7. What does `id` do, and how is it different from `name`?
8. True or False: HTML controls how a page looks (colors, fonts, spacing).

---

## Module Checklist

- [ ] I created `public/index.html` with login and register forms.
- [ ] I created `public/dashboard.html` with a task form and an empty task list.
- [ ] I viewed both pages successfully in a browser (even though unstyled).
- [ ] I completed all 3 practice exercises.
- [ ] I can explain tags, elements, and attributes without looking at notes.

---

## What's Next

Open `04-css-fundamentals.md` to make these pages look like a real app instead of plain text.

---

## Quiz Answer Key

1. A tag is the bracketed markup itself (e.g., `<p>`); an element is the tag plus its content and closing tag (e.g., `<p>Hello</p>`).
2. `<head>` holds metadata not directly shown on the page (title, character set, stylesheet links); `<body>` holds everything visibly rendered to the user.
3. It links the label to a specific input by matching the input's `id`, so clicking the label focuses/activates that input (also important for accessibility).
4. `type="password"` visually masks the characters typed (showing dots/asterisks) so the password isn't visible on-screen.
5. It tells the browser to block form submission until that field has a value, providing basic built-in validation.
6. E.g. `<nav>` communicates "this block contains navigation links," giving it meaning beyond a generic container.
7. `id` must be unique on the page and is used to target one specific element (via CSS/JavaScript); `name` is the key used to identify a form field's value when the form data is submitted, and can repeat (e.g., across radio buttons in the same group).
8. False — that is CSS's job; HTML only describes structure and content.
