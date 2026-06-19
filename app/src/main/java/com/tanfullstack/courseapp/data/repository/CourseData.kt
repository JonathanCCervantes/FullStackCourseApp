package com.tanfullstack.courseapp.data.repository

import com.tanfullstack.courseapp.data.models.*

object CourseData {

    fun getAllModules(): List<Module> = listOf(

        Module(
            id = "module_intro",
            title = "Introduction",
            description = "Start your full-stack journey. Learn the basics of the web and set up your tools.",
            icon = "🏁",
            color = "#4CAF50",
            order = 1,
            lessons = listOf(
                Lesson("intro_01", "Course Overview", "00-course-overview.md", 5, false, false, 1),
                Lesson("intro_02", "How the Web Works", "01-how-the-web-works.md", 10, true, false, 2),
                Lesson("intro_03", "Setup & Installation", "02-setup-and-installation.md", 15, false, false, 3)
            )
        ),

        Module(
            id = "module_frontend_basics",
            title = "Frontend Basics",
            description = "Master the core languages of the web: HTML, CSS, and JavaScript.",
            icon = "🎨",
            color = "#E44D26",
            order = 2,
            lessons = listOf(
                Lesson("html_01", "HTML Fundamentals", "03-html-fundamentals.md", 20, true, true, 1),
                Lesson("css_01", "CSS Fundamentals", "04-css-fundamentals.md", 20, true, true, 2),
                Lesson("js_01", "JavaScript Fundamentals", "05-javascript-fundamentals.md", 25, true, true, 3)
            )
        ),

        Module(
            id = "module_vcs",
            title = "Version Control",
            description = "Learn to track your code and collaborate using Git.",
            icon = "🔀",
            color = "#F05032",
            order = 3,
            lessons = listOf(
                Lesson("git_01", "Git & Version Control", "06-git-version-control.md", 15, true, false, 1)
            )
        ),

        Module(
            id = "module_backend_basics",
            title = "Backend with Node.js",
            description = "Build servers and learn the fundamentals of Node.js and Express.",
            icon = "🟢",
            color = "#68A063",
            order = 4,
            lessons = listOf(
                Lesson("node_01", "Node.js Fundamentals", "07-nodejs-fundamentals.md", 20, true, true, 1),
                Lesson("express_01", "Express.js Fundamentals", "08-expressjs-fundamentals.md", 20, true, true, 2)
            )
        ),

        Module(
            id = "module_databases",
            title = "Databases & Integration",
            description = "Learn to store data in MongoDB and connect it to your backend.",
            icon = "🗄️",
            color = "#336791",
            order = 5,
            lessons = listOf(
                Lesson("db_01", "MongoDB Fundamentals", "09-mongodb-fundamentals.md", 20, true, true, 1),
                Lesson("db_02", "Connecting Express & MongoDB", "10-connecting-express-mongodb.md", 25, true, true, 2)
            )
        ),

        Module(
            id = "module_frontend_integration",
            title = "Frontend Integration",
            description = "Bring it all together by connecting your UI to your backend APIs.",
            icon = "🔗",
            color = "#61DAFB",
            order = 6,
            lessons = listOf(
                Lesson("front_01", "Frontend API Integration", "11-frontend-api-integration.md", 25, true, true, 1)
            )
        ),

        Module(
            id = "module_advanced",
            title = "Advanced Full Stack",
            description = "Secure your app, learn testing, and build your capstone project.",
            icon = "🛠️",
            color = "#9C27B0",
            order = 7,
            lessons = listOf(
                Lesson("adv_01", "Authentication", "12-authentication.md", 30, true, true, 1),
                Lesson("adv_02", "Testing & Debugging", "13-testing-debugging.md", 20, true, false, 2),
                Lesson("adv_03", "Capstone Project", "14-capstone-project.md", 60, false, true, 3)
            )
        ),

        Module(
            id = "module_deployment",
            title = "Launch",
            description = "Deploy your application to the world and plan your next steps.",
            icon = "🚀",
            color = "#9B59B6",
            order = 8,
            lessons = listOf(
                Lesson("deploy_01", "Next Steps & Deployment", "15-next-steps-and-deployment.md", 20, false, false, 1)
            )
        )
    )
}