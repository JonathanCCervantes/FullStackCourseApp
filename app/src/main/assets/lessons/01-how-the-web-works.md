# Module 01: How the Web Works & Full-Stack Concepts

## Learning Objectives

By the end of this module, you will be able to:
- Explain what happens between typing a URL and seeing a page.
- Define client, server, request, response, and API in plain English.
- Explain what a REST API is and why it matters for this course.
- Understand why we split apps into "frontend" and "backend."

## Estimated Time
45–60 minutes (no coding yet — this is the mental model for everything else).

---

## Concepts

### 1. The Client-Server Model

Every website works on a simple back-and-forth:

- **Client**: the program asking for something. Usually your web browser (Chrome, Firefox, Safari, Edge).
- **Server**: a program running on a computer somewhere, waiting for requests, and sending back answers.

Think of it like a restaurant:
- You (the client) tell the waiter (the request) what you want.
- The kitchen (the server) prepares it.
- The waiter brings back your food (the response).

When you type `www.example.com` into your browser:
1. Your browser sends a **request** asking "please send me this page."
2. A server somewhere receives that request.
3. The server decides what to send back (an HTML page, some data, an error, etc.).
4. The server sends a **response**.
5. Your browser receives the response and draws it on your screen.

In this course, **you will write the server** (using Node.js + Express) and **you will write the client-facing pages** (using HTML/CSS/JavaScript). You control both ends.

### 2. HTTP — the Language of the Web

Requests and responses follow a shared set of rules called **HTTP** (HyperText Transfer Protocol). A few things to know now:

- Every request has a **method** describing the intent:
  - `GET` — "give me data" (e.g., load a page, fetch a list of tasks).
  - `POST` — "here is new data, create something with it" (e.g., submit a new task).
  - `PUT` / `PATCH` — "update this existing thing."
  - `DELETE` — "remove this thing."
- Every request has a **URL/path** describing what it's about, e.g. `/tasks` or `/tasks/42`.
- Every response has a **status code**, a 3-digit number summarizing what happened:
  - `200` — OK, success.
  - `201` — Created successfully.
  - `400` — Bad request (client sent something wrong).
  - `401` — Unauthorized (not logged in).
  - `404` — Not found.
  - `500` — Server error (something broke on the server).

You don't need to memorize these yet — you'll see them constantly once you start building, and they'll become second nature.

### 3. Frontend vs. Backend

| | Frontend | Backend |
|---|---|---|
| Runs where? | In the user's browser | On a server |
| Languages we use | HTML, CSS, JavaScript | JavaScript (via Node.js) |
| Job | Display things, capture clicks/input | Business logic, talk to the database, security |
| Can the user see the code? | Yes — anyone can view it in their browser | No — it stays private on the server |

This is **why passwords and sensitive logic always live in the backend**: anything in frontend code is visible to anyone who opens their browser's developer tools. You'll see this distinction matter a lot in Module 12 (Authentication).

### 4. What Is an API?

**API** stands for Application Programming Interface. In web development, it almost always means: a set of URLs that the backend exposes, that the frontend (or anything else) can send requests to, to get or change data — without needing a full web page back, just raw data (commonly in a format called **JSON**).

Example: instead of the server sending back a whole HTML page, your TaskFlow backend will expose something like:
- `GET /api/tasks` → returns a list of tasks as JSON.
- `POST /api/tasks` → creates a new task from JSON you send.
- `DELETE /api/tasks/:id` → deletes one task.

This style — using HTTP methods and URLs to represent actions on resources — is called a **REST API** (REST = Representational State Transfer). It's the most common API style in the industry, and it's exactly what you'll build starting in Module 8.

### 5. What Is JSON?

**JSON** (JavaScript Object Notation) is a simple text format for representing data, that both humans and computers can read. Example of a single task as JSON:

```json
{
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "completed": false
}
```

It looks almost exactly like a JavaScript object (you'll learn those in Module 5) — that's intentional, and it's why JSON is so natural to use across the whole stack: frontend JavaScript, the Express backend, and even MongoDB all speak this same "shape" of data.

### 6. What Is a Database?

A database is a program specialized in storing data permanently and efficiently, and letting other programs ask for it back later (even after the computer restarts). Without a database, any data your app "remembers" would disappear the moment the server restarts, because it would only exist in the computer's temporary memory (RAM).

You will use **MongoDB**, a database that stores data as JSON-like documents — which is why it pairs so naturally with JavaScript.

### 7. Putting It All Together: The Full Request Lifecycle

Here is the exact journey one action will take in your finished TaskFlow app — for example, clicking "Add Task":

1. You type a task title into a form in the browser and click "Add."
2. **Frontend JavaScript** catches that click, packages the title into JSON, and sends a `POST` request to `/api/tasks` on your server.
3. **Express** (backend) receives that request.
4. Express hands the data to **Mongoose** (a tool that talks to MongoDB on Node's behalf).
5. **MongoDB** saves the new task permanently to disk.
6. MongoDB confirms back to Mongoose → Express.
7. Express sends a response (e.g., status `201 Created`, with the saved task as JSON) back to the browser.
8. **Frontend JavaScript** receives that response and updates the page to show the new task — without reloading the whole page.

Every module from here builds toward you being able to write every single piece of that chain yourself.

---

## Hands-On: Explore It Live (No Coding Yet)

This exercise just makes the concepts above tangible using tools already on your computer.

1. Open your web browser.
2. Go to any website, for example `https://example.com`.
3. Open **Developer Tools**:
   - Chrome/Edge: press `F12`, or right-click the page → "Inspect."
   - Firefox: press `F12`, or right-click → "Inspect."
   - Safari: enable the Develop menu first in Preferences → Advanced → "Show Develop menu," then `Cmd+Option+I`.
4. Click the **Network** tab in Developer Tools.
5. Reload the page (`Ctrl+R` or `Cmd+R`).
6. You will see a list of requests appear. Click on the first one (usually the page's own address).
7. Look for these fields and find them on your screen:
   - **Request Method** (should say `GET`).
   - **Status Code** (should say `200`).
   - **Response Headers** — find one called `Content-Type`.
8. Click on the **Response** (or "Preview") tab for that request — you are looking at the raw HTML the server sent back, before your browser turned it into a visual page.

This is exactly the request/response cycle you just read about, happening for real, right now, on your machine.

---

## Practice Exercises

1. Repeat the steps above on two different websites. Write down (in a notes app or on paper) the status code you saw for the main page request on each.
2. In the Network tab, find any request whose method is **not** `GET` (hint: try a site with a search box — search for something and watch the Network tab while submitting).
3. In plain English, write 2–3 sentences describing what a server is, without using the words "computer" or "internet."

---

## Quiz

1. What is the role of the "client" in the client-server model?
2. Name the HTTP method you would use to create a new resource.
3. What does a status code of `404` mean?
4. True or False: Frontend code is hidden from users and cannot be viewed by them.
5. What does API stand for, and in your own words, what is it for?
6. What format does data typically take when sent between frontend and backend in a REST API?
7. Why can't an app remember data permanently without a database?
8. List the three layers of a full-stack app and one tool/language used for each, as taught in this course.

---

## Module Checklist

- [ ] I can explain client vs. server in my own words.
- [ ] I can name at least 3 HTTP methods and what each is for.
- [ ] I opened Developer Tools and viewed a real request/response in the Network tab.
- [ ] I understand what JSON looks like.
- [ ] I understand why a database is necessary.
- [ ] I completed all 3 practice exercises.
- [ ] I attempted the quiz before checking the answer key.

---

## What's Next

In `02-setup-and-installation.md`, you will install every tool needed to start writing real code: a code editor, Node.js, Git, and MongoDB — all free, all local, no accounts required.

---

## Quiz Answer Key

1. The client is the program that initiates a request and consumes the response — typically the web browser, asking for data or pages on the user's behalf.
2. `POST`.
3. The requested resource was not found on the server.
4. False — frontend code is sent to and runs in the user's browser, so it can always be viewed (e.g., via "View Page Source" or Developer Tools).
5. API = Application Programming Interface. It's a defined set of URLs/methods a backend exposes so other programs (like a frontend) can request or change data without needing a full webpage.
6. JSON.
7. Without a database, data only exists in the server's temporary memory (RAM), which is cleared whenever the server process stops or restarts.
8. Frontend (HTML/CSS/JavaScript), Backend (Node.js + Express), Database (MongoDB).
