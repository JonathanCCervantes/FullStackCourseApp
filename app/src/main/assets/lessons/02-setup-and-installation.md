# Module 02: Installing Your Tools

## Learning Objectives

By the end of this module you will have, on your own computer:
- A code editor (VS Code) installed and open.
- Node.js and npm installed and verified.
- Git installed and verified.
- MongoDB Community Server installed and running locally.
- A project folder created and ready for TaskFlow.

No account, sign-up, or login is required for anything in this module.

## Estimated Time
45–90 minutes (mostly downloads/installer wizards).

---

## Concepts

- **Code editor**: a text editor built for writing code (syntax highlighting, autocomplete, built-in terminal). We use **VS Code** (Visual Studio Code), free and made by Microsoft, no account needed.
- **Node.js**: a program that lets JavaScript run outside the browser — directly on your computer, including as a server. **npm** (Node Package Manager) comes bundled with it, and is used to install reusable code libraries.
- **Git**: a tool that tracks changes to your code over time (a "version control system"), so you can save checkpoints and undo mistakes. We use it **fully locally** — no GitHub account required for this course.
- **MongoDB Community Server**: the free, self-hosted version of MongoDB that runs on your own machine.
- **Terminal / Command Line**: a text-based way to give your computer instructions, instead of clicking icons. You will use this constantly from here on.

---

## Step-by-Step Hands-On

### Step 1: Open a Terminal

You will use a terminal throughout this entire course. Learn where it is now:

- **Windows**: Press `Windows key`, type `PowerShell`, press Enter. (Avoid the old "Command Prompt" — use PowerShell.)
- **macOS**: Press `Cmd+Space`, type `Terminal`, press Enter.
- **Linux**: Press `Ctrl+Alt+T`, or open "Terminal" from your applications menu.

Keep this window open — you'll use it in every step below that has a code block.

### Step 2: Install Visual Studio Code

1. Open your browser and go to: `https://code.visualstudio.com`
2. Click the big download button (it auto-detects your OS).
3. Run the downloaded installer:
   - **Windows**: double-click the `.exe` file. Accept the license, keep all default options checked (including "Add to PATH"), click **Next** through the wizard, then **Install**, then **Finish**.
   - **macOS**: open the downloaded `.zip`, drag `Visual Studio Code.app` into your **Applications** folder.
   - **Linux**: download the `.deb` file, then in your terminal run:
     ```
     sudo apt install ./Downloads/code_*.deb
     ```
4. Open VS Code (Start Menu on Windows, Applications on macOS, app launcher on Linux).
5. Confirm it opened successfully — you should see a "Welcome" tab.

### Step 3: Install Node.js (and npm)

1. Go to: `https://nodejs.org`
2. Download the **LTS** version (LTS = Long Term Support — the stable recommended one, NOT "Current").
3. Run the installer:
   - **Windows**: double-click the `.msi` file. Click **Next** through all default options, then **Install**, then **Finish**.
   - **macOS**: double-click the `.pkg` file. Follow the installer prompts with default options, enter your computer password when asked, click **Close** when done.
   - **Linux (Ubuntu/Debian)**: in your terminal, run:
     ```
     curl -fsSL https://deb.nodesource.com/setup_lts.x | sudo -E bash -
     sudo apt install -y nodejs
     ```
4. **Verify the install.** Open (or re-open) your terminal and type exactly:
   ```
   node -v
   ```
   Press Enter. You should see a version number like `v20.x.x` or `v22.x.x`.
5. Then type:
   ```
   npm -v
   ```
   Press Enter. You should see a version number like `10.x.x`.

   If either command says "command not found" / "not recognized," close and fully reopen your terminal, then try again. If it still fails, restart your computer and try again (this re-applies the PATH change the installer made).

### Step 4: Install Git

1. Go to: `https://git-scm.com/downloads`
2. Download for your OS.
3. Run the installer:
   - **Windows**: double-click the `.exe`. Click **Next** through every screen, keeping all default options (the defaults are correct for this course). Click **Install**, then **Finish**.
   - **macOS**: double-click the `.dmg`, run the package installer inside, follow prompts with defaults.
   - **Linux**:
     ```
     sudo apt install git
     ```
4. **Verify.** In your terminal:
   ```
   git --version
   ```
   You should see something like `git version 2.4x.x`.

5. **Set your Git identity** (used only to label your own local commits — not an account, just a name/email label stored on your machine). In your terminal, run both lines, replacing the example text with your own name/email:
   ```
   git config --global user.name "Your Name"
   git config --global user.email "you@example.com"
   ```
   No verification email is sent — this is purely local metadata.

### Step 5: Install MongoDB Community Server (Local Database)

**Windows:**
1. Go to: `https://www.mongodb.com/try/download/community`
2. Version: leave as the latest stable. Platform: Windows. Package: **msi**. Click **Download**.
3. Run the `.msi` file.
4. In the setup wizard, choose **Complete** setup type.
5. When asked about "Service Configuration," leave it checked to **"Install MongoD as a Service"** with **"Run service as Network Service user"** — this makes MongoDB start automatically in the background whenever your computer starts, so you never have to manually start it.
6. Leave **"Install MongoDB Compass"** checked (this is a free graphical tool to view your database visually — no account needed to use it locally).
7. Click **Install**, then **Finish**.

**macOS (using Homebrew):**
1. If you don't have Homebrew, install it first by pasting this into your terminal:
   ```
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```
   Follow any on-screen instructions it prints (it may ask you to run 1–2 more lines — copy and run exactly what it tells you).
2. Then run:
   ```
   brew tap mongodb/brew
   brew install mongodb-community@7.0
   ```
3. Start MongoDB as a background service so it always runs:
   ```
   brew services start mongodb-community@7.0
   ```

**Linux (Ubuntu):**
1. Run these commands one at a time:
   ```
   sudo apt-get install gnupg curl
   curl -fsSL https://pgp.mongodb.com/server-7.0.asc | sudo gpg -o /usr/share/keyrings/mongodb-server-7.0.gpg --dearmor
   echo "deb [signed-by=/usr/share/keyrings/mongodb-server-7.0.gpg] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
   sudo apt-get update
   sudo apt-get install -y mongodb-org
   ```
2. Start it and set it to always run:
   ```
   sudo systemctl start mongod
   sudo systemctl enable mongod
   ```

**Verify MongoDB is running (all OSes):**

1. Install `mongosh` (the MongoDB Shell) if it wasn't bundled: go to `https://www.mongodb.com/try/download/shell`, download for your OS, and run the installer with default options.
2. In your terminal, type:
   ```
   mongosh
   ```
3. You should see it connect and print something like `Current Mongosh Log ID:` followed by a prompt that looks like `test>`. This means MongoDB is running locally and you successfully connected to it.
4. Type `exit` and press Enter to leave the shell.

If `mongosh` says it cannot connect: on Windows, open the **Services** app (search for it in the Start Menu), find "MongoDB Server," right-click → **Start**. On macOS/Linux, re-run the `brew services start` or `systemctl start mongod` command from above.

### Step 6: Install One VS Code Extension You'll Need Later (optional now)

You can skip this until Module 13, but if you want to do it now:
1. Open VS Code.
2. Click the **Extensions** icon in the left sidebar (it looks like 4 squares).
3. Search for **Thunder Client**.
4. Click **Install** on the extension by "rangav."

This lets you test your backend API directly inside VS Code later — no account or login needed.

### Step 7: Create Your Project Folder

1. In your terminal, navigate to wherever you want to keep your coding projects. For example, to use your Documents folder:
   - **Windows**:
     ```
     cd $HOME\Documents
     ```
   - **macOS/Linux**:
     ```
     cd ~/Documents
     ```
2. Create the project folder and move into it:
   ```
   mkdir taskflow
   cd taskflow
   ```
3. Open this folder in VS Code directly from the terminal:
   ```
   code .
   ```
   (If `code` is not recognized on macOS: in VS Code, press `Cmd+Shift+P`, type "Shell Command: Install 'code' command in PATH", press Enter, then restart your terminal and try again.)
4. VS Code should now open with an empty `taskflow` folder shown in the Explorer panel on the left. This is your project home for the rest of the course.

---

## Practice Exercises

1. Close your terminal completely, reopen it, and re-run `node -v`, `npm -v`, and `git --version` from memory (no copy-paste) to build muscle memory.
2. Open MongoDB Compass (if installed) and connect to `mongodb://localhost:27017` (the default local address) — just confirm it connects, you should see a "no databases yet" or default system databases listed.
3. Inside the `taskflow` folder, use VS Code's built-in terminal (`Terminal` menu → `New Terminal`) instead of your OS terminal, and run `node -v` again there — confirm it gives the same result. This is the terminal you'll use going forward.

---

## Quiz

1. What is the difference between Node.js and npm?
2. Why do we install the "LTS" version of Node.js rather than "Current"?
3. What command checks your installed Git version?
4. What does running `mongosh` do, and what does seeing a `test>` prompt confirm?
5. Why didn't we need to create any account in this entire module?
6. What is MongoDB Compass, and is it required?
7. What command opens the current folder in VS Code from the terminal?

---

## Module Checklist

- [ ] VS Code is installed and opens successfully.
- [ ] `node -v` and `npm -v` both return version numbers.
- [ ] `git --version` returns a version number, and I set my name/email with `git config`.
- [ ] MongoDB is installed and `mongosh` successfully connects.
- [ ] I created a `taskflow` folder and opened it in VS Code.
- [ ] I completed all 3 practice exercises.

---

## What's Next

Open `03-html-fundamentals.md` — you'll write your first real files and create the visual skeleton of the TaskFlow app.

---

## Quiz Answer Key

1. Node.js is the program that runs JavaScript outside the browser; npm is the package manager bundled with it, used to install and manage reusable code libraries.
2. LTS versions receive long-term stability and security updates and are recommended for most users; "Current" includes newer, less-tested features.
3. `git --version`.
4. `mongosh` opens the MongoDB Shell and connects to your local MongoDB server; seeing the `test>` prompt confirms MongoDB is installed, running, and reachable on your machine.
5. Everything was installed and run entirely on your own computer (local development) — no cloud service or login was needed at any point.
6. MongoDB Compass is a free graphical tool for visually browsing/editing your local MongoDB data. It is optional — you can do everything via `mongosh` or code instead, but it's helpful for beginners to see their data visually.
7. `code .` (the dot means "this current folder").
