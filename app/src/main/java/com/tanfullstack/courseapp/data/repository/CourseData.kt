package com.tanfullstack.courseapp.data.repository

import com.tanfullstack.courseapp.data.models.*

object CourseData {

    fun getAllModules(): List<Module> = listOf(

        Module(
            id = "module_html",
            title = "HTML Fundamentals",
            description = "Build the structure of the web. Learn tags, elements, and semantic HTML.",
            icon = "🌐",
            color = "#E44D26",
            order = 1,
            lessons = listOf(
                Lesson("html_01", "What is HTML?", "html_01_intro.md", 10, true, false, 1),
                Lesson("html_02", "HTML Tags & Elements", "html_02_tags.md", 15, true, true, 2),
                Lesson("html_03", "Forms & Inputs", "html_03_forms.md", 20, true, true, 3),
                Lesson("html_04", "Semantic HTML", "html_04_semantic.md", 15, true, false, 4),
                Lesson("html_05", "HTML Tables", "html_05_tables.md", 10, true, true, 5)
            )
        ),

        Module(
            id = "module_css",
            title = "CSS Styling",
            description = "Make the web beautiful. Master selectors, layouts, and responsive design.",
            icon = "🎨",
            color = "#264DE4",
            order = 2,
            lessons = listOf(
                Lesson("css_01", "CSS Basics & Selectors", "css_01_basics.md", 15, true, false, 1),
                Lesson("css_02", "Box Model & Spacing", "css_02_boxmodel.md", 15, true, true, 2),
                Lesson("css_03", "Flexbox Layout", "css_03_flexbox.md", 20, true, true, 3),
                Lesson("css_04", "CSS Grid", "css_04_grid.md", 20, true, true, 4),
                Lesson("css_05", "Responsive Design", "css_05_responsive.md", 20, true, true, 5),
                Lesson("css_06", "CSS Animations", "css_06_animations.md", 15, true, false, 6)
            )
        ),

        Module(
            id = "module_js",
            title = "JavaScript",
            description = "Add interactivity to the web. From variables to async programming.",
            icon = "⚡",
            color = "#F7DF1E",
            order = 3,
            lessons = listOf(
                Lesson("js_01", "JS Basics: Variables & Types", "js_01_basics.md", 15, true, false, 1),
                Lesson("js_02", "Functions & Scope", "js_02_functions.md", 20, true, true, 2),
                Lesson("js_03", "Arrays & Objects", "js_03_arrays_objects.md", 20, true, true, 3),
                Lesson("js_04", "DOM Manipulation", "js_04_dom.md", 25, true, true, 4),
                Lesson("js_05", "Events & Listeners", "js_05_events.md", 20, true, true, 5),
                Lesson("js_06", "Promises & Async/Await", "js_06_async.md", 25, true, true, 6),
                Lesson("js_07", "Fetch API & REST", "js_07_fetch.md", 20, true, true, 7),
                Lesson("js_08", "ES6+ Modern JavaScript", "js_08_es6.md", 20, true, false, 8)
            )
        ),

        Module(
            id = "module_react",
            title = "React",
            description = "Build powerful UIs with components, state, hooks, and React Router.",
            icon = "⚛️",
            color = "#61DAFB",
            order = 4,
            lessons = listOf(
                Lesson("react_01", "React Introduction & JSX", "react_01_intro.md", 20, true, false, 1),
                Lesson("react_02", "Components & Props", "react_02_components.md", 20, true, true, 2),
                Lesson("react_03", "State & useState Hook", "react_03_state.md", 25, true, true, 3),
                Lesson("react_04", "useEffect & Lifecycle", "react_04_useeffect.md", 25, true, true, 4),
                Lesson("react_05", "React Router", "react_05_router.md", 20, true, true, 5),
                Lesson("react_06", "Context API", "react_06_context.md", 20, true, false, 6),
                Lesson("react_07", "Fetching Data in React", "react_07_data.md", 20, true, true, 7)
            )
        ),

        Module(
            id = "module_node",
            title = "Node.js & Express",
            description = "Build the backend. Servers, routing, middleware, and APIs.",
            icon = "🟢",
            color = "#68A063",
            order = 5,
            lessons = listOf(
                Lesson("node_01", "Node.js Introduction", "node_01_intro.md", 15, true, false, 1),
                Lesson("node_02", "npm & Modules", "node_02_npm.md", 15, true, false, 2),
                Lesson("node_03", "Express Framework", "node_03_express.md", 25, true, true, 3),
                Lesson("node_04", "Routing & Controllers", "node_04_routing.md", 20, true, true, 4),
                Lesson("node_05", "Middleware", "node_05_middleware.md", 20, true, false, 5),
                Lesson("node_06", "REST API Design", "node_06_restapi.md", 25, true, true, 6),
                Lesson("node_07", "Authentication & JWT", "node_07_auth.md", 30, true, true, 7)
            )
        ),

        Module(
            id = "module_db",
            title = "Databases",
            description = "Store and query data. SQL fundamentals and MongoDB basics.",
            icon = "🗄️",
            color = "#336791",
            order = 6,
            lessons = listOf(
                Lesson("db_01", "Database Concepts", "db_01_concepts.md", 15, true, false, 1),
                Lesson("db_02", "SQL Basics", "db_02_sql.md", 25, true, true, 2),
                Lesson("db_03", "SQL Joins & Relations", "db_03_joins.md", 25, true, true, 3),
                Lesson("db_04", "MongoDB Basics", "db_04_mongodb.md", 20, true, true, 4),
                Lesson("db_05", "Mongoose & Node.js", "db_05_mongoose.md", 20, true, true, 5)
            )
        ),

        Module(
            id = "module_git",
            title = "Git & Version Control",
            description = "Track changes, collaborate, and use GitHub like a pro.",
            icon = "🔀",
            color = "#F05032",
            order = 7,
            lessons = listOf(
                Lesson("git_01", "Git Basics", "git_01_basics.md", 15, true, false, 1),
                Lesson("git_02", "Branching & Merging", "git_02_branches.md", 20, true, true, 2),
                Lesson("git_03", "GitHub & Remote Repos", "git_03_github.md", 15, true, false, 3),
                Lesson("git_04", "Pull Requests & Workflow", "git_04_pullrequests.md", 15, true, false, 4)
            )
        ),

        Module(
            id = "module_deploy",
            title = "Deployment & DevOps",
            description = "Get your app live. Hosting, CI/CD, environment variables, and Docker basics.",
            icon = "🚀",
            color = "#9B59B6",
            order = 8,
            lessons = listOf(
                Lesson("deploy_01", "Hosting Options Overview", "deploy_01_hosting.md", 15, true, false, 1),
                Lesson("deploy_02", "Deploying with Vercel/Netlify", "deploy_02_vercel.md", 20, true, true, 2),
                Lesson("deploy_03", "Environment Variables", "deploy_03_env.md", 15, true, false, 3),
                Lesson("deploy_04", "Docker Basics", "deploy_04_docker.md", 25, true, false, 4),
                Lesson("deploy_05", "CI/CD Pipelines", "deploy_05_cicd.md", 20, true, false, 5)
            )
        )
    )
}