[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=15006063&assignment_repo_type=AssignmentRepo)
# COSC3056 Week 05 Workshop Starter Code
Starter code for Week 05 Workshop for Javalin. This project will introduce how to use the Javalin web server library to run a Java program as a web server. This allows you to create a Java program to "output" HTML to a web server, and then view this HTML in a web browser! This follows on from the Week 04 example which introduced JDBC. It uses the same Movies SQL database, but instead produces HTML output.

Folders:
```bash
├── /src/main                    - Location of all files as required by build configuration
│         ├── java               - Java Source location
│         │    └── app           - package location for all Java files
│         └── resources          - Web resources (html templates / style sheets)
│               ├── css          - CSS Style-sheets
│               └── images       - Image files
│ 
├── /target                      - build directory (DO NOT MODIFY)
├── /database                    - The folder to store sqlite database files (*.db files)
├── pom.xml                      - Configure Build (DO NOT MODIFY)
└── README.md                    - This file ;)
```

Libraries:
* org.xerial.sqlite-jdbc (SQLite JDBC library)
* javalin (lightweight Java Webserver)

Libraries required as dependencies:
* By javalin
   * slf4j-simple (lightweight logging)
* By xerial/jdbc
   * sqlite-jdbc

# Building & Running the code
1. Open this project within VSCode
2. Allow VSCode to read the pom.xml file
 - Allow the popups to run and "say yes" to VSCode configuring the build
 - Allow VSCode to download the required Java libraries
3. To Build & Run
 - Open the src/main/java/app/App.java source file, and select "Run" from the pop-up above the main function
4. Go to: http://localhost:7001

# Important Notes
1. ONLY modify the files which you are allowed to edit. The other files are placed in important places to make our "big software project" work.
2. DO NOT move the Java files from the ```src/main/java/app``` folder. These Java files need to be in this location to ensure our "big software project" works.

# DEV Container for GitHub Codespaces
The ```.devcontainer``` folder contains configuration files for GitHub Codespaces.
This ensures that when the GitHub classroom is cloned, the workspace is correctly configured for Java (V16) and with the required VSCode extensions.
This folder will not affect a *local* VSCode setup on a computer.

**🚨 DO NOT MODIFY THE CONTENTS OF THIS FOLDER. 🚨**

# Authors
* COSC3056 teaching team, School of Science and Technologies, RMIT Vietnam.
* Dr. Timothy Wiley, School of Computing Technologies, STEM College, RMIT University.
* Prof. Santha Sumanasekara, School of Computing Technologies, STEM College, RMIT University.

Copyright RMIT University (c) 2023

