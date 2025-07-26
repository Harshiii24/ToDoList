# 📝 To-Do List Application

A simple **standalone Java Swing To-Do List desktop application** that helps you manage your daily tasks efficiently. It supports creating, editing, sorting, filtering, and persisting tasks with priority and due date.

---

## 📦 About the Project

This application is built using **pure Java (no external frameworks)** and runs as a standalone program on any system with Java installed. It uses Java Swing for the graphical user interface and Java Serialization for saving/loading data locally.

---

## 🎯 Features

- ✅ Add new tasks with description, due date, and priority  
- ✅ Mark tasks as complete or incomplete  
- 🛠️ Edit and delete existing tasks  
- 🔍 Search tasks by description (live filtering)  
- 🗂️ Filter tasks: All | Active | Completed  
- 🔃 Sort tasks by: Due Date | Priority  
- 🎨 Visual priority color-coding:
  - 🔴 High → Red
  - 🟡 Medium → Yellow
  - 🟢 Low → Green
- 💾 Data persistence using local file (`tasks.dat`)  
- 🖥️ Standalone app – no installation required

---

## 🧱 Project Structure

```
.
├── Priority.java               # Enum for task priority
├── SerializationManager.java  # Handles saving/loading tasks
├── Task.java                  # Serializable Task class
├── TaskCellRenderer.java      # Custom renderer for JList cells
└── ToDoListApp.java           # Main class with GUI and logic
```

---

## 🚀 How to Run

### ✅ Requirements

- Java Development Kit (JDK) 8 or later

### 💻 Running from Terminal

1. Open terminal in project directory.
2. Compile the source files:

   ```bash
   javac *.java
   ```

3. Run the application:

   ```bash
   java ToDoListApp
   ```

### 🧰 Running from an IDE

1. Open the project in IntelliJ, Eclipse, or any Java-compatible IDE.
2. Run `ToDoListApp.java`.

---

## 🗂️ Data Persistence

- Tasks are automatically **saved on exit** to a file named `tasks.dat`.
- On next launch, the application loads tasks from this file.
- No external databases or setup required.

---

## 📌 Usage Tips

- 🖱️ **Click a task** to:
  - Edit
  - Mark as complete/incomplete
  - Delete

- 🔍 **Search field** filters tasks live as you type.

- 📅 **Due date selector** uses a calendar-style spinner.

- 🧹 Tasks are **visually styled** based on their priority and completion status.

---

## 📸 UI Snapshot (Optional)

_Add screenshots or a GIF preview here if desired._

---

## ⚙️ Technologies Used

- Java 8+
- Java Swing (GUI)
- Java Serialization (data persistence)

---

## 🙋‍♂️ Author

Developed by **Harshal Patil**  
Feel free to fork and improve!
