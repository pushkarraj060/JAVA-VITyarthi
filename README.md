# Smart Campus Lost & Found

A Java OOP based campus Lost and Found Management System.

## Features

- Student registration and login
- Admin login
- Report lost items
- Report found items
- Search items
- View personal reports
- Automatic possible-match scoring
- Submit claims
- Admin approval/rejection
- Mark items as returned
- System statistics
- File-based persistent storage

## Technologies

- Java
- Object-Oriented Programming
- Collections
- File Handling
- Exception Handling

## Default Admin

Email: admin@campus.com
Password: admin123

## Run

Compile all Java files from the project root:

```bash
javac -d out src/model/*.java src/service/*.java src/SmartCampusLostAndFound/Main.java
```

Run:

```bash
java -cp out SmartCampusLostAndFound.Main
```

The application automatically creates the data folder and text files.
