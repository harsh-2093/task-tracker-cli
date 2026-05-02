**Project URL:** [https://roadmap.sh/projects/task-tracker](https://roadmap.sh/projects/task-tracker)
# Task Tracker CLI

A simple, dependency-free Command Line Interface (CLI) application built in pure Java to manage tasks. This project uses no external libraries and saves data directly to a local `task.json` file.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher installed on your computer.

### How to Compile
Open your terminal in the project folder and run:
```bash
javac TaskTracker.java Task.java

Commands

1. Add a Task
Creates a new task. The ID is generated automatically.

java TaskTracker add "Finish the Java project"

2. List Tasks
Shows a formatted table of all tasks. You can optionally filter by status.

# View all tasks
java TaskTracker list

# View tasks by specific status
java TaskTracker list todo
java TaskTracker list in-progress
java TaskTracker list done

3. Update a Task
Updates the status or description of a specific task using its ID.

# Update the status
java TaskTracker update <id> status in-progress

# Update the description
java TaskTracker update <id> description "Need to learn Spring Boot"

4. Delete a Task
Removes a task completely from your file.

java TaskTracker delete <id>

How It Works
This project is built to demonstrate core Java fundamentals:

Zero Libraries: Instead of using tools like Gson or Jackson, this app parses JSON manually to teach you how data serialization works from scratch.

Direct File Manipulation: The app reads task.json as raw text, searches for specific data, and updates the text directly.

Safe Rebuilding: When you update or delete a task, the application intelligently rebuilds the JSON structure element by element to ensure commas and brackets are perfectly placed without corrupting the file.

Author: Harsh
