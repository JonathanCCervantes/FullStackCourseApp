package com.tanfullstack.courseapp.data.repository

import com.tanfullstack.courseapp.data.models.Challenge

object ChallengeData {

    fun getChallengeForLesson(lessonId: String): Challenge? {
        return when (lessonId) {

            "html_02" -> Challenge(
                id = "challenge_html_02",
                lessonId = lessonId,
                title = "Build a Personal Bio Page",
                description = """
                    Create an HTML page that includes:
                    1. A main heading with your name (use <h1>)
                    2. A subheading that says "About Me" (use <h2>)
                    3. A paragraph describing yourself (use <p>)
                    4. An unordered list of 3 of your hobbies (use <ul> and <li>)
                    5. A link to any website (use <a> with href)
                """.trimIndent(),
                starterCode = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <title>My Bio Page</title>
                    </head>
                    <body>
                        <!-- Write your HTML here -->
                        
                    </body>
                    </html>
                """.trimIndent(),
                hints = listOf(
                    "Hint 1: Start with <h1>Your Name</h1> inside the <body> tags.",
                    "Hint 2: For a list, use <ul> as the wrapper and <li> for each item.",
                    "Hint 3: An anchor tag looks like: <a href='https://example.com'>Click here</a>"
                ),
                expectedOutputDescription = "A page with your name as a big heading, an 'About Me' section, a paragraph, a list of hobbies, and a working link.",
                solution = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <title>My Bio Page</title>
                    </head>
                    <body>
                        <h1>John Doe</h1>
                        <h2>About Me</h2>
                        <p>I am a web developer learning full-stack development.</p>
                        <ul>
                            <li>Coding</li>
                            <li>Reading</li>
                            <li>Gaming</li>
                        </ul>
                        <a href="https://www.google.com">Visit Google</a>
                    </body>
                    </html>
                """.trimIndent()
            )

            "js_02" -> Challenge(
                id = "challenge_js_02",
                lessonId = lessonId,
                title = "Write a Temperature Converter",
                description = """
                    Write a JavaScript function called convertTemp that:
                    1. Takes two parameters: a number (temperature) and a string (unit: 'C' or 'F')
                    2. If unit is 'C', converts Celsius to Fahrenheit: (C × 9/5) + 32
                    3. If unit is 'F', converts Fahrenheit to Celsius: (F - 32) × 5/9
                    4. Returns the converted value rounded to 1 decimal place
                    5. Call the function and log the result to the console
                """.trimIndent(),
                starterCode = """
                    function convertTemp(temperature, unit) {
                        // Write your code here
                        
                    }
                    
                    // Test your function:
                    console.log(convertTemp(100, 'C')); // Should print 212
                    console.log(convertTemp(32, 'F'));  // Should print 0
                """.trimIndent(),
                hints = listOf(
                    "Hint 1: Use an if/else statement to check if unit === 'C'.",
                    "Hint 2: Use .toFixed(1) to round to 1 decimal place: result.toFixed(1)",
                    "Hint 3: Remember to use return to send the result back from the function."
                ),
                expectedOutputDescription = "convertTemp(100, 'C') should return '212.0' and convertTemp(32, 'F') should return '0.0'.",
                solution = """
                    function convertTemp(temperature, unit) {
                        if (unit === 'C') {
                            return ((temperature * 9/5) + 32).toFixed(1);
                        } else {
                            return ((temperature - 32) * 5/9).toFixed(1);
                        }
                    }
                    
                    console.log(convertTemp(100, 'C')); // 212.0
                    console.log(convertTemp(32, 'F'));  // 0.0
                """.trimIndent()
            )

            else -> null
        }
    }
}