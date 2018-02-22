# Overview

## Modules

1. core - Shared model/api
2. authServer - Authentication server application
3. console (TBD)
4. romeo - Web application backed by Spring Security and https
5. juliet - Web application (no Spring Security or https)
6. tools - Test tools

# Quick Start

Whenever a username and password is asked, the only valid user is `admin` regardless of the password.

1. Navigate into `certs`
2. Create SSL certs: `makecert`
3. Build: `gradle`
4. Navigate into `build`
5. Untar webapp: `tar xvf romeo-0.1.tar`
6. Untar webapp: `tar xvf juliet-0.1.tar`
7. Start up both webapps by navigating to their respective `bin` directory and executing the runscript
8. Browser: https://localhost:8991/romeo
9. Browser: http://localhost:8992/juliet
