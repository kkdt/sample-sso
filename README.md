# Overview

The goal for this project is to explore single-sign-on where an existing desktop/thick client application (`console`) authenticate users but also provides the identity to web applications to facility single sign-on. An authenticated session is always initiated by the thick client application; however, web applications should provide the same authentication mechanism should the user directly access the webapp on a browser.

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
2. Create SSL certs: `makecert` (need to do this first)
3. Build: `gradle`
4. Untar authServer, console, and webapps: `tar xvf <tarball>` (located in `build`)
5. Start up `authServer`
6. Start up `console`
7. Start up both webapps
8. Browser (to romeo): https://localhost:8991
9. Browser (to juliet): http://localhost:8992

# Single Sign-On

1. Log into the Console
2. Copy the id_token from the feedback area
3. Launch the URL: `https://localhost:8991/auth?id=<token>` - Assure that you are authenticated

# TODO

There is an additional endpoint at `romeo` to test authenticated REST calls - `https://localhost:8991/echo?message=message`
