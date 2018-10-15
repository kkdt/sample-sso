# Overview

The goal for this project is to explore single sign-on where an existing desktop/thick client application (`console`) authenticates users via a 1st-part relationship with a couple of backend services but also performs single sing-on to external web applications when launching a browser from `console`. The external webapp in this project is `romeo`; `console` and `juliet` are 1st-part applications; and the `authServer` provides common authentication and OpenID token retrieval via RMI interfaces for all applications.

The crypto is RSA with all services sharing the same keystore; however, this can be extended to each server having their own keystore so long as there is a way to retrieve each server's public key. Crypto is doing a decryption and checking signature but in a production environment, the recipient should also verify the claims in the JWT appropriately.

# Modules

1. core - Shared model/api
2. core-security - Spring Security support
3. authServer - Authentication server application (RMI interface)
4. console - Thick client application using Spring Security for authentication
5. romeo - Spring Boot Web application backed by Spring Security and https
6. juliet - Spring Boot Web application (https)
7. tools - Test tools

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
9. Browser (to juliet): https://localhost:8992

# Single Sign-On 1

1. Log into the Console either via 'jws' or 'jwe'
2. Copy the id_token from the feedback area
3. Launch `romeo` by pasting the token to the URL text area and click 'Launch' (the text area is for demonstration purposes only)

```
Endpoint - https://localhost:8991/jws?token=<token>
Endpoint - https://localhost:8991/jwe?token=<token>
```

The problem - in both the JWS and JWE situation, data is transfered via URL parameters which can be intercepted. For example, just copy and paste the URL in a separate browser.

# Single Sign-On 2

1. Log into Console by choosing 'sso'
2. DO NOT modify the URL text area
3. Note that URL is pointing to `juliet` - the internal webapp
4. Launch `romeo` by clicking 'Launch'

```
Endpoint - https://localhost:8992/login?url=http://localhost:8991?sso&user=admin
```

In this scenario, Juliet is an internal webapp with Console and is the proxy for redirecting the browser to an endpoint in Romeo that expects an encrypted cookie from an external source. This cookie will be used to authenticate the user who launched the browser from the desktop application. The cookie is generated, encrypted, and signed via Juliet and is sent with the redirect.