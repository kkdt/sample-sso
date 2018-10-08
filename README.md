# Overview

Suppose there already exist some legacy application system where users interact with desktop/thick clients. The legacy client-service and service-service interfaces are established - all security concerns have been addressed. An internal authentication service facilitates users logging into client applications.

The goal for this project is to explore single-sign-on where an existing desktop/thick client application (`console`) authenticate users but also provides some identity federation to web applications launched in the same session. An authenticated session is always initiated by the thick client application; however, web applications should provide the same authentication mechanism should the user directly access the webapp on a browser.

The primary focus of the project is to implement and address the security concerns when an authenticated client application launches a browser to a web application.

# Modules

1. core - Shared model/api
2. authServer - Authentication server application (RMI interface)
3. console - Thick client application using Spring Security for authentication
4. romeo - Spring Boot Web application backed by Spring Security and https
5. juliet - Spring Boot Web application (no Spring Security or https)
6. tools - Test tools

# Quick Start

Whenever a username and password is asked, the only valid user is `admin`; any password. Each application's run script is bundled in their respective tarball located at `<app>/bin` when extracted.

1. Navigate into `certs`
2. Create SSL certs: `makecert`
3. Build: `gradle`
4. Navigate into `build`
5. Untar authServer, console, and webapps: `tar xvf <tarball>`
6. Start up `authServer`
7. Start up `console`
8. Start up both webapps
9. Browser: https://localhost:8991/romeo
10. Browser: http://localhost:8992/juliet

# issue a GET request to see the (empty) list of tasks
curl http://localhost:8080/tasks

# issue a POST request to create a new task
curl -H "Content-Type: application/json" -X POST -d '{
    "description": "Buy some milk(shake)"
}'  http://localhost:8080/tasks

# issue a PUT request to update the recently created task
curl -H "Content-Type: application/json" -X PUT -d '{
    "description": "Buy some milk"
}'  http://localhost:8080/tasks/1

# issue a DELETE request to remove the existing task
curl -X DELETE http://localhost:8080/tasks/1

