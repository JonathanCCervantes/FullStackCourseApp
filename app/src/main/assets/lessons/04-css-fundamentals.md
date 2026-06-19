# Module 04: CSS Fundamentals

## Learning Objectives

By the end of this module you will be able to:
- Explain selectors, properties, and values.
- Understand and use the CSS box model.
- Use Flexbox to lay out elements.
- Have built a real stylesheet that makes TaskFlow look like a presentable app.

## Estimated Time
3–5 hours.

---

## Concepts

### What Is CSS?

**CSS** (Cascading Style Sheets) controls the visual presentation of HTML: colors, fonts, spacing, layout, sizing, responsiveness.

### Anatomy of a CSS Rule

```css
selector {
  property: value;
  property: value;
}
```

Example:
```css
h1 {
  color: blue;
  font-size: 32px;
}
```
This says: "for every `<h1>` element, make the text color blue and the font size 32 pixels."

### Three Ways to Apply CSS (we use the third)

1. Inline: `<p style="color: red;">` — avoid, hard to maintain.
2. Internal: a `<style>` block inside `<head>` — okay for tiny tests.
3. **External file**, linked via `<link rel="stylesheet" href="style.css">` — what real projects use, and what we already linked in Module 03.

### Selectors

| Selector | Matches | Example |
|---|---|---|
| Tag name | All elements of that tag | `p { }` matches every `<p>` |
| `.class` | Elements with that class attribute | `.error-message { }` matches `<p class="error-message">` |
| `#id` | The one element with that id | `#login-form { }` matches `<form id="login-form">` |
| `,` (comma) | Combine multiple selectors | `h1, h2 { }` |
| Descendant (space) | Elements nested inside another | `nav a { }` matches `<a>` tags inside `<nav>` |

Classes (`class="..."`) can be reused on many elements; IDs (`id="..."`) must be unique per page. Prefer classes for styling, IDs for JavaScript targeting and unique anchors.

### The Box Model

Every HTML element is a rectangular box made of four layers, from inside out:

1. **Content** — the text/image itself.
2. **Padding** — space between the content and the border (inside the box).
3. **Border** — a line around the padding.
4. **Margin** — space outside the border, between this box and others.

```css
.card {
  padding: 16px;
  border: 1px solid #ccc;
  margin: 8px;
}
```

`box-sizing: border-box;` (commonly applied globally) makes `width`/`height` include padding and border, which is far more predictable and is what we'll use.

### Common Properties

| Property | Purpose |
|---|---|
| `color` | Text color |
| `background-color` | Background fill color |
| `font-size`, `font-family`, `font-weight` | Text appearance |
| `width`, `height` | Box dimensions |
| `padding`, `margin` | Spacing (see box model) |
| `border`, `border-radius` | Borders, rounded corners |
| `display` | How an element lays out (`block`, `inline`, `flex`, `none`, etc.) |

### Flexbox — Your Main Layout Tool

Flexbox arranges children of a container in a row or column, with easy alignment and spacing.

```css
.container {
  display: flex;
  flex-direction: row;       /* or column */
  justify-content: space-between; /* spacing along main axis */
  align-items: center;       /* alignment along cross axis */
  gap: 12px;                 /* space between children */
}
```

This single tool solves the vast majority of beginner layout problems — centering things, evenly spacing items in a row, stacking things vertically with consistent gaps.

### Responsive Basics: Media Queries

```css
@media (max-width: 600px) {
  .dashboard-main {
    flex-direction: column;
  }
}
```
This block of CSS only applies when the browser window is 600px wide or less (e.g., phones), letting you adapt layout to screen size.

---

## Step-by-Step Hands-On: Style TaskFlow

### Step 1: Create `public/style.css`

1. Create a new file: `public/style.css`.
2. Paste this exact content and save:

```css
/* ---------- Global Reset & Base Styles ---------- */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: -apple-system, "Segoe UI", Roboto, Arial, sans-serif;
  background-color: #f4f6f8;
  color: #1f2933;
  line-height: 1.5;
  padding: 24px;
}

h1 {
  font-size: 28px;
  margin-bottom: 4px;
}

h2 {
  font-size: 18px;
  margin-bottom: 12px;
  color: #334155;
}

button {
  cursor: pointer;
  background-color: #2563eb;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 10px 16px;
  font-size: 14px;
  font-weight: 600;
}

button:hover {
  background-color: #1d4ed8;
}

input {
  display: block;
  width: 100%;
  padding: 10px;
  margin: 6px 0 16px 0;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 14px;
}

label {
  font-size: 13px;
  font-weight: 600;
  color: #475569;
}

.error-message {
  color: #dc2626;
  font-size: 13px;
  margin-top: 8px;
  min-height: 18px;
}

/* ---------- Auth Page (index.html) ---------- */
.auth-page {
  max-width: 400px;
  margin: 40px auto;
  background: white;
  padding: 32px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.subtitle {
  color: #64748b;
  margin-bottom: 24px;
}

#login-section {
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #e2e8f0;
}

/* ---------- Dashboard (dashboard.html) ---------- */
.dashboard-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 700px;
  margin: 0 auto 24px auto;
  background: white;
  padding: 16px 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.dashboard-main {
  max-width: 700px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.new-task-section,
.task-list-section {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

#task-list {
  list-style: none;
}

.task-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  margin-bottom: 8px;
}

.task-item.completed {
  opacity: 0.6;
  text-decoration: line-through;
}

/* ---------- Responsive ---------- */
@media (max-width: 480px) {
  body {
    padding: 12px;
  }
  .auth-page,
  .new-task-section,
  .task-list-section,
  .dashboard-header {
    padding: 16px;
  }
}
```

### Step 2: Reload and Compare

1. Reload `index.html` in your browser (or it auto-refreshes if using Live Server).
2. You should now see a centered white card, styled blue button, styled inputs.
3. Reload `dashboard.html` — same visual treatment should apply.

If styles don't appear: confirm `style.css` is inside `public/` (same folder as the HTML files), and that the `<link>` tag in your HTML says exactly `href="style.css"`.

---

## Practice Exercises

1. Change `background-color: #2563eb;` on `button` to a different hex color of your choice, save, and confirm the button color changes on reload.
2. Add a CSS rule using the `.task-item.completed` selector pattern style you already have, but invent a new class `.task-item.high-priority` that sets `border-left: 4px solid #dc2626;` — this previews how you'll visually flag priority later.
3. Resize your browser window narrower than 480px wide (or use Developer Tools' device toolbar, `Ctrl+Shift+M` / `Cmd+Shift+M`) and confirm the responsive padding change from the media query takes effect.

---

## Quiz

1. What are the four layers of the CSS box model, from inside to outside?
2. What's the difference between a class selector and an ID selector?
3. What does `display: flex` do to the direct children of an element?
4. What CSS property creates rounded corners?
5. What is a media query used for?
6. Why do we prefer linking an external `.css` file over inline `style="..."` attributes?
7. In the box model, does `padding` go inside or outside the border?

---

## Module Checklist

- [ ] I created `public/style.css` and linked it correctly to both HTML pages.
- [ ] Both pages visually changed after adding the stylesheet.
- [ ] I can explain the box model in my own words.
- [ ] I used Flexbox at least once and understand `justify-content` vs `align-items`.
- [ ] I completed all 3 practice exercises.

---

## What's Next

Open `05-javascript-fundamentals.md` — you'll learn the programming language that brings both the frontend and (later) the backend to life.

---

## Quiz Answer Key

1. Content, padding, border, margin.
2. A class selector (`.name`) can match many elements and be reused; an ID selector (`#name`) should match exactly one unique element on the page.
3. It arranges them as flexible items in a row or column (depending on `flex-direction`), enabling easy alignment and spacing via properties like `justify-content`, `align-items`, and `gap`.
4. `border-radius`.
5. To apply different CSS rules depending on conditions like screen width, enabling responsive designs that adapt to different devices.
6. External files are reusable across multiple HTML pages, keep markup clean, and are easier to maintain than scattering styles inline throughout the HTML.
7. Padding goes inside the border (between content and border); margin goes outside the border.
