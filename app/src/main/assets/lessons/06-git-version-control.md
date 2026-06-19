# Module 06: Git Version Control (Local Only)

## Learning Objectives

By the end of this module you will be able to:
- Explain why version control matters.
- Initialize a Git repository and make commits.
- Check status, history, and differences.
- Use a `.gitignore` file correctly.
- Create and merge a branch.

This entire module uses Git **100% locally** — no GitHub account, no sign-up, no internet connection required.

## Estimated Time
1.5–2.5 hours.

---

## Concepts

### What Problem Does Git Solve?

Without version control, "saving your work" only gives you one current state — if you break something, there's no way back except memory or manual backup copies (`project_v2_FINAL_fixed.zip`). Git lets you:
- Save labeled checkpoints ("commits") of your entire project at any point.
- See exactly what changed, and when.
- Go back to any previous checkpoint safely.
- Experiment on a separate "branch" without risking your working code.

### Key Vocabulary

| Term | Meaning |
|---|---|
| **Repository (repo)** | A project folder that Git is tracking |
| **Commit** | A saved snapshot of the project at a point in time, with a message describing it |
| **Staging area** | A holding area where you choose exactly which changes to include in the next commit |
| **Branch** | An independent line of development; the default is usually called `main` |
| **Working directory** | Your actual files, as they currently sit on disk |

### The Git Workflow

```
working directory  --git add-->  staging area  --git commit-->  repository history
```

You edit files → `git add` selects which edited files to include → `git commit` permanently saves that selected snapshot with a message.

---

## Step-by-Step Hands-On

### Step 1: Initialize the Repository

In your terminal, inside the `taskflow` folder:

```
git init
```

You should see a message like `Initialized empty Git repository in .../taskflow/.git/`. This created a hidden `.git` folder that stores all history — never delete it manually.

### Step 2: Check Status

```
git status
```

This shows every file Git has noticed that isn't committed yet — right now, that should be everything you've built so far (`public/index.html`, `public/dashboard.html`, `public/style.css`, `public/app.js`), listed as "Untracked files."

### Step 3: Create a `.gitignore` File

Some files should never be committed (we'll see exactly why once we reach Module 7 and create a `node_modules` folder and a `.env` file holding secrets). Create them now so you don't forget:

1. Create a new file at the project root (same level as `public/`, NOT inside it): `.gitignore`
2. Paste this exact content:

```
node_modules/
.env
.DS_Store
*.log
```

- `node_modules/` — a folder of downloaded code libraries (Module 7) — huge, and instantly recreatable from a list, so it's never committed.
- `.env` — will hold secret configuration values (Module 9/12) — must never be committed or shared.
- `.DS_Store` — a harmless macOS system file, not needed in the repo.
- `*.log` — log files generated while running the app.

### Step 4: Stage and Commit Everything

```
git add .
```

The `.` means "everything in this folder and subfolders" (respecting `.gitignore`). Confirm what's staged:

```
git status
```

Files should now show under "Changes to be committed," in green.

Now commit:

```
git commit -m "Initial commit: HTML structure, CSS styling, and JS fundamentals practice"
```

`-m` provides the commit message inline. Good commit messages are short, present-tense, and describe *what* changed.

### Step 5: View History

```
git log
```

You'll see your commit with a unique ID (a long string of letters/numbers called a "hash"), your name/email, the date, and the message. Press `q` to exit the log view if it doesn't return automatically.

For a compact one-line view (useful once you have many commits):

```
git log --oneline
```

### Step 6: Make a Change and See the Diff

1. Open `public/style.css`, change the `body { background-color: #f4f6f8; }` value to `#eef2ff`, save.
2. Run:
   ```
   git status
   ```
   `style.css` now shows as "Changes not staged for commit," in red.
3. Run:
   ```
   git diff
   ```
   This shows exactly which line changed, old vs. new (red = removed, green = added). Press `q` to exit if needed.
4. Stage and commit this specific change:
   ```
   git add public/style.css
   git commit -m "Tweak dashboard background color"
   ```

### Step 7: Branching (Practicing Safe Experimentation)

Create and switch to a new branch to try something without affecting `main`:

```
git branch experiment-dark-mode
git switch experiment-dark-mode
```

(Older Git tutorials use `git checkout experiment-dark-mode` — `git switch` is the newer, clearer command for the same purpose.)

Confirm which branch you're on at any time:

```
git branch
```

The current branch has an asterisk `*` next to it.

Make an experimental change: open `style.css`, change `body { color: #1f2933; }` to `body { color: #f1f5f9; }`, save, then:

```
git add public/style.css
git commit -m "Experiment: light text color"
```

Switch back to `main`:

```
git switch main
```

Notice `style.css` reverts to its previous text color when you reload the page — your experiment is safely isolated on the other branch.

### Step 8: Merge the Branch Back (Since the Experiment Was Good)

If you decide you actually want that experimental change in `main`:

```
git switch main
git merge experiment-dark-mode
```

Git combines the histories. Confirm with `git log --oneline` that the experimental commit is now part of `main`'s history too.

### Step 9 (Optional, For Awareness Only): GitHub

GitHub is a website that hosts Git repositories remotely, mainly so you can back up your code online and collaborate with others. **It requires creating a free account**, which is why it is intentionally outside the scope of this course. Everything you've done in this module works completely without it. If you choose to use GitHub later, the core commands you already know (`add`, `commit`, `branch`, `merge`) stay exactly the same — you'd just add `git push`/`git pull` to sync with the remote copy.

---

## Practice Exercises

1. Make three more small, separate changes to any file (e.g., text tweaks), committing each one separately with a clear message. Then run `git log --oneline` and confirm you see all of them listed in order.
2. Create a new branch called `experiment-button-color`, change the button background color, commit it, switch back to `main`, and confirm the button color in `main` is unaffected.
3. Run `git status` right now with no changes pending, and read/understand the exact message Git shows when there's nothing to commit.

---

## Quiz

1. What is the difference between the working directory, the staging area, and the repository?
2. What command shows you exactly which lines changed in a modified file?
3. Why do we add `node_modules/` to `.gitignore`?
4. What does `git switch <branch-name>` do?
5. What is the purpose of a commit message?
6. True or False: You must have a GitHub account to use Git.
7. What command merges one branch's changes into the branch you're currently on?

---

## Module Checklist

- [ ] `git init` was run successfully in the `taskflow` folder.
- [ ] `.gitignore` exists with the four entries shown above.
- [ ] I made at least 3 separate, meaningfully-named commits.
- [ ] I created a branch, committed on it, switched away, switched back, and merged it.
- [ ] I can read `git log --oneline` output and explain what it shows.

---

## What's Next

Open `07-nodejs-fundamentals.md` — you'll start writing backend code with Node.js, the foundation for everything from here through Module 14.

---

## Quiz Answer Key

1. The working directory is your actual files on disk as currently edited; the staging area is a holding zone where you select exactly which changes will go into the next commit (via `git add`); the repository is the permanent, saved history of commits (via `git commit`).
2. `git diff`.
3. `node_modules/` contains downloaded third-party code libraries that can be huge and are easily and exactly re-downloaded from `package.json`/`package-lock.json` (Module 7), so committing them would bloat the repository unnecessarily.
4. It switches your working directory to reflect a different branch's latest commit, so you can work on that line of development independently.
5. It records, in human-readable form, what changed and why, making project history understandable later (by you or anyone else).
6. False — Git itself is fully local and works with zero accounts; GitHub is a separate, optional hosting service that does require an account.
7. `git merge <branch-name>`.
