# Full-Stack Web Development for Complete Beginners
### Node.js + Express + MongoDB + HTML/CSS/JavaScript

---

## Welcome

This is a complete, self-contained, text-only course that takes you from **zero coding experience** to having built a real, working full-stack web application from scratch on your own computer.

You will build one project all the way through the course: **TaskFlow**, a task/to-do manager web app with user accounts, where each user can create, view, edit, complete, and delete their own tasks.

By the end, you will understand and have hand-typed every layer of a real web app:
- The browser-facing frontend (HTML, CSS, JavaScript)
- The server (Node.js + Express)
- The database (MongoDB)
- How user accounts and passwords are handled securely
- How to test and debug your own code

**No account creation is required anywhere in the core course.** Everything runs on your own computer (this is called "local development"). The one optional appendix on cloud deployment (Module 14) is clearly marked optional and tells you upfront that it requires a free account with a hosting provider — you can read it for awareness and skip it entirely.

No videos. Every instruction is written out in full, precise, step-by-step text so it can be followed exactly, by a human or fed into an AI coding agent as a build script.

---

## Who This Course Is For

- You have **never coded before**, or only dabbled.
- You learn best from precise, ordered, written instructions rather than video.
- You want to understand *why*, not just copy-paste.

## What You Need Before Starting

- A Windows, macOS, or Linux computer that you can install free software on.
- Internet access (only for downloading free tools — not for using any paid/account-based service).
- No prior coding knowledge required.

## What "Full-Stack" Means

A web application has three main layers:

| Layer | What it does | Tools we use |
|---|---|---|
| **Frontend** | What the user sees and clicks in the browser | HTML, CSS, JavaScript |
| **Backend** | The server that handles logic, security, and data requests | Node.js + Express |
| **Database** | Where data (tasks, users) is permanently stored | MongoDB |

"Full-stack" means you can build all three layers yourself. That's exactly what this course teaches.

---

## How This Course Is Structured

Every module file follows the same pattern, so you always know what to expect:

1. **Learning Objectives** — what you'll be able to do after this module.
2. **Concepts** — plain-English explanations, no jargon left unexplained.
3. **Step-by-Step Hands-On** — exact commands to type, exact files to create, exact code to write, exact menus to click. Every file path is explicit.
4. **Practice Exercises** — small tasks to cement the lesson, separate from the main project.
5. **Quiz** — questions to check your understanding.
6. **Quiz Answer Key** — at the very end of the file, clearly marked, so you can test yourself honestly before checking.
7. **Module Checklist** — tick boxes to confirm you completed everything.
8. **What's Next** — bridge to the following module.

**Rule for hands-on sections:** every time you need to create a file, the instructions say exactly: the file's full path/name, the full contents to put in it, and where to save it. Every time you need to run a command, it is shown in a code block exactly as you should type it into your terminal.

---

## Full Table of Contents

| # | File | Module | Builds Toward TaskFlow |
|---|---|---|---|
| 00 | `00-course-overview.md` | This file | — |
| 01 | `01-how-the-web-works.md` | How the Web Works & Full-Stack Concepts | Mental model |
| 02 | `02-setup-and-installation.md` | Installing Your Tools | Dev environment |
| 03 | `03-html-fundamentals.md` | HTML Fundamentals | TaskFlow page structure |
| 04 | `04-css-fundamentals.md` | CSS Fundamentals | TaskFlow styling |
| 05 | `05-javascript-fundamentals.md` | JavaScript Fundamentals | Core programming skills |
| 06 | `06-git-version-control.md` | Git Version Control (local only) | Save TaskFlow's history |
| 07 | `07-nodejs-fundamentals.md` | Node.js Fundamentals | Backend runtime basics |
| 08 | `08-expressjs-fundamentals.md` | Express.js Fundamentals | TaskFlow server skeleton |
| 09 | `09-mongodb-fundamentals.md` | MongoDB Fundamentals | TaskFlow database |
| 10 | `10-connecting-express-mongodb.md` | Connecting Express + MongoDB | TaskFlow Task API (CRUD) |
| 11 | `11-frontend-api-integration.md` | Frontend ↔ API Integration | TaskFlow becomes dynamic |
| 12 | `12-authentication.md` | User Accounts & Authentication | TaskFlow login/register |
| 13 | `13-testing-debugging.md` | Testing & Debugging | Verify TaskFlow works |
| 14 | `14-capstone-project.md` | Capstone: Finish & Extend TaskFlow | Final polished app |
| 15 | `15-next-steps-and-deployment.md` | Next Steps & Optional Deployment | Where to go from here |

Estimated total time: **60–90 hours**, depending on pace. Go as slow as you need — there are no deadlines.

---

## The Final Project: TaskFlow

By Module 14 you will have a working app where a user can:
1. Visit a web page and register an account (username + password, stored securely).
2. Log in.
3. Add a new task with a title and description.
4. See a list of all their own tasks.
5. Mark a task complete, edit it, or delete it.
6. Log out.
7. All data is permanently saved in a MongoDB database on their computer, and survives closing/reopening the app.

Every module adds one real piece of this app. Nothing is thrown away.

---

## A Note on Using This Course With an AI Coding Agent

Every hands-on step in this course is written as an explicit, literal instruction (exact file path → exact full file contents → exact terminal command). This is intentional: these files can be used as a structured build specification for an AI coding agent, with each module representing one milestone/checkpoint. If you are pairing this course with an agent, work through one module's hands-on section at a time and verify the checklist before moving to the next module.

---

## Start Here

Open `01-how-the-web-works.md` next.
