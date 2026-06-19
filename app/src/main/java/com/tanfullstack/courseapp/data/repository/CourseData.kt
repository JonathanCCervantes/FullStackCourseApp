package com.tanfullstack.courseapp.data.repository

import com.tanfullstack.courseapp.data.models.*

object CourseData {

    fun getAllModules(): List<Module> = listOf(

        Module(
            id = "module_intro",
            title = "Getting Started",
            description = "Welcome to the Full Stack Web Development course. Start here!",
            icon = "👋",
            color = "#4CAF50",
            order = 1,
            lessons = listOf(
                Lesson("intro_01", "Course Overview", "00-course-overview.md", 5, false, false, 1),
                Lesson("intro_02", "How the Web Works", "01-how-the-web-works.md", 10, true, false, 2),
                Lesson("intro_03", "Setup & Installation", "02-setup-and-installation.md", 15, false, false, 3)
            )
        ),

        Module(
            id = "module_html",
            title = "HTML Fundamentals",
            description = "Build the structure of the web. Learn tags, elements, and semantic HTML.",
            icon = "🌐",
            color = "#E44D26",
            order = 2,
            lessons = listOf(
                Lesson("html_01", "HTML Basics", "03-html-fundamentals.md", 15, true, true, 1)
            )
        ),

        Module(
            id = "module_css",
            title = "CSS Styling",
            description = "Make the web beautiful. Master selectors, layouts, and responsive design.",
            icon = "🎨",
            color = "#264DE4",
            order = 3,
            lessons = listOf(
                Lesson("css_01", "CSS Fundamentals", "04-css-fundamentals.md", 20, true, true, 1)
            )
        ),

        Module(
            id = "module_js",
            title = "JavaScript",
            description = "Add interactivity to the web. From variables to async programming.",
            icon = "⚡",
            color = "#F7DF1E",
            order = 4,
            lessons = listOf(
                Lesson("js_01", "JavaScript Fundamentals", "05-javascript-fundamentals.md", 25, true, true, 1)
            )
        ),

        Module(
            id = "module_git",
            title = "Git & Version Control",
            description = "Track changes, collaborate, and use GitHub like a pro.",
            icon = "🔀",
            color = "#F05032",
            order = 5,
            lessons = listOf(
                Lesson("git_01", "Git Basics", "06-git-version-control.md", 15, true, false, 1)
            )
        ),

        Module(
            id = "module_node",
            title = "Node.js & Express",
            description = "Build the backend. Servers, routing, and APIs.",
            icon = "🟢",
            color = "#68A063",
            order = 6,
            lessons = listOf(
                Lesson("node_01", "Node.js Fundamentals", "07-nodejs-fundamentals.md", 20, true, true, 1),
                Lesson("node_02", "Express.js Fundamentals", "08-expressjs-fundamentals.md", 20, true, true, 2)
            )
        ),

        Module(
            id = "module_db",
            title = "Databases",
            description = "Store and query data. MongoDB and Mongoose basics.",
            icon = "🗄️",
            color = "#336791",
            order = 7,
            lessons = listOf(
                Lesson("db_01", "MongoDB Fundamentals", "09-mongodb-fundamentals.md", 20, true, true, 1),
                Lesson("db_02", "Connecting Express & MongoDB", "10-connecting-express-mongodb.md", 25, true, true, 2)
            )
        ),

        Module(
            id = "module_frontend",
            title = "Frontend Integration",
            description = "Connect your frontend to the backend APIs.",
            icon = "🔗",
            color = "#61DAFB",
            order = 8,
            lessons = listOf(
                Lesson("front_01", "Frontend API Integration", "11-frontend-api-integration.md", 25, true, true, 1)
            )
        ),

        Module(
            id = "module_advanced",
            title = "Advanced Topics",
            description = "Authentication, Testing, and Capstone project.",
            icon = "🛠️",
            color = "#9C27B0",
            order = 9,
            lessons = listOf(
                Lesson("adv_01", "Authentication", "12-authentication.md", 30, true, true, 1),
                Lesson("adv_02", "Testing & Debugging", "13-testing-debugging.md", 20, true, false, 2),
                Lesson("adv_03", "Capstone Project", "14-capstone-project.md", 60, false, true, 3)
            )
        ),

        Module(
            id = "module_deploy",
            title = "Deployment & Next Steps",
            description = "Get your app live and plan your future career.",
            icon = "🚀",
            color = "#9B59B6",
            order = 10,
            lessons = listOf(
                Lesson("deploy_01", "Next Steps & Deployment", "15-next-steps-and-deployment.md", 20, false, false, 1)
            )
        )
    )
}