# Overview

The goal for this project is to explore single sign-on between two applications - a thick client (`console`) and a web application (`romeo`). The end goal would be to have the end-user authenticate via the thick client and its backend; click a button that points to the webapp and be fully authenticated.

## Assumptions

1. Both applications are in a closed environment
2. There is PKI implemented within the environment; each server containing signed server certificates; common CA
3. Both application authenticate its users via a common authentication service
4. Both applications has access to the other public certificate
5. Crypto is RSA

## Scenario Description

Common Services

- Authentication Service (RMI interface), `authServer`
- Identity Broker (RMI interface), `authServer`

> For the purpose of prototyping, these interfaces are exposed and started in the same module.

Application 1

- Console, `console`
- Juliet/Identity Provider (https/webapp), `juliet`

Application 2

- Romeo (https/webapp), `romeo`

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
Romeo Endpoint - https://localhost:8991/jws?token=<token>
Romeo Endpoint - https://localhost:8991/jwe?token=<token>
```

The problem - in both the JWS and JWE situation, data is transfered via URL parameters which can be intercepted. For example, just copy and paste the URL in a separate browser.

# Single Sign-On 2

1. Log into Console by choosing 'sso'
2. DO NOT modify the URL text area
3. Note that URL is pointing to `juliet` - the internal webapp/proxy
4. Launch `romeo` by clicking 'Launch'

```
Juliet Endpoint - https://localhost:8992/login?url=http://localhost:8991/sso&user=admin
```

> The URL is simple for the purpose of prototyping. For example, the redirect URL could be stored and not exposed in the URL.

In this scenario, suppose Juliet is an *internal* (closed to the outside world) webapp and is the proxy for redirecting the browser to an endpoint. The redirect goes to Romeo and expects an encrypted cookie from an external source. This cookie is the id_token and will be used to authenticate the user. The cookie is generated, encrypted, and signed via Juliet and is sent with the redirect. Furthermore, the request is over SSL and is encrypted on transport. 

Because Juliet is an internal 1st-party webapp and also an identity provider, the assumption here is that it has an implicit flow for obtaining ID tokens which is provided directly with the redirection response. Another assumption is that the Console has to run on the same server as Juliet so a remote display flow for starting up the Console.

# Questions

1. Delete cookie on log out
2. Browser launched from server vs. end user workstation