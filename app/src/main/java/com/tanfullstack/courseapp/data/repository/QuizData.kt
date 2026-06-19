package com.tanfullstack.courseapp.data.repository

import com.tanfullstack.courseapp.data.models.Quiz
import com.tanfullstack.courseapp.data.models.QuizQuestion

object QuizData {

    fun getQuizForLesson(lessonId: String): Quiz? {
        return when (lessonId) {

            "html_01" -> Quiz(
                id = "quiz_html_01",
                lessonId = lessonId,
                questions = listOf(
                    QuizQuestion("q1", "What does HTML stand for?",
                        listOf("Hyper Text Markup Language", "High Tech Modern Language",
                            "Hyper Transfer Markup Language", "Home Tool Markup Language"),
                        0, "HTML stands for HyperText Markup Language — the standard language for creating web pages."),
                    QuizQuestion("q2", "Which tag is used for the largest heading?",
                        listOf("<h6>", "<heading>", "<h1>", "<head>"),
                        2, "<h1> is the largest heading tag. Headings go from h1 (largest) to h6 (smallest)."),
                    QuizQuestion("q3", "Where is the correct place to add a <script> tag?",
                        listOf("In the <head> only", "In the <body> only",
                            "Either in <head> or before closing </body>", "Outside the HTML document"),
                        2, "Scripts can go in the <head> or before the closing </body> tag. Modern practice prefers before </body> for performance."),
                    QuizQuestion("q4", "What is the correct HTML element for a paragraph?",
                        listOf("<para>", "<p>", "<pg>", "<text>"),
                        1, "<p> is the paragraph tag in HTML."),
                    QuizQuestion("q5", "Which attribute is used to define an image source?",
                        listOf("href", "link", "src", "url"),
                        2, "The src attribute specifies the path to the image file in an <img> tag.")
                )
            )

            "css_01" -> Quiz(
                id = "quiz_css_01",
                lessonId = lessonId,
                questions = listOf(
                    QuizQuestion("q1", "What does CSS stand for?",
                        listOf("Computer Style Sheets", "Cascading Style Sheets",
                            "Creative Style System", "Color Style Sheets"),
                        1, "CSS stands for Cascading Style Sheets — used to style HTML elements."),
                    QuizQuestion("q2", "Which property changes the text color?",
                        listOf("font-color", "text-color", "color", "foreground"),
                        2, "The 'color' property in CSS sets the text color of an element."),
                    QuizQuestion("q3", "How do you select an element with id='header'?",
                        listOf(".header", "#header", "header", "*header"),
                        1, "The # symbol selects elements by their id in CSS."),
                    QuizQuestion("q4", "Which CSS property controls the space inside an element?",
                        listOf("margin", "spacing", "padding", "border"),
                        2, "Padding controls the space INSIDE an element, between content and border."),
                    QuizQuestion("q5", "Which value makes a flex container?",
                        listOf("display: block", "display: grid", "display: flex", "display: inline"),
                        2, "Setting display: flex on a container enables Flexbox layout.")
                )
            )

            "js_01" -> Quiz(
                id = "quiz_js_01",
                lessonId = lessonId,
                questions = listOf(
                    QuizQuestion("q1", "Which keyword declares a variable that cannot be reassigned?",
                        listOf("var", "let", "const", "static"),
                        2, "const declares a constant — a variable that cannot be reassigned after declaration."),
                    QuizQuestion("q2", "What does typeof 'hello' return?",
                        listOf("string", "text", "char", "String"),
                        0, "typeof returns the type as a lowercase string. For 'hello', it returns 'string'."),
                    QuizQuestion("q3", "Which is NOT a JavaScript data type?",
                        listOf("Boolean", "Float", "String", "Undefined"),
                        1, "JavaScript has: String, Number, Boolean, Null, Undefined, Object, Symbol. 'Float' is not a type — numbers are just 'Number'."),
                    QuizQuestion("q4", "What is the result of 5 + '3' in JavaScript?",
                        listOf("8", "53", "Error", "NaN"),
                        1, "JavaScript coerces 5 to a string and concatenates — resulting in '53'."),
                    QuizQuestion("q5", "Which method adds an item to the end of an array?",
                        listOf("append()", "push()", "add()", "insert()"),
                        1, "array.push() adds one or more items to the end of an array.")
                )
            )

            // Add more quizzes following the same pattern for other lessons
            else -> null
        }
    }
}