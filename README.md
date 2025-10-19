# Simple single sign-in with Oauth

In order to run, the following steps need to be followed:
1. Create a postgres connection and replace the username, host, password correctly in application.yml
2. create a private/key pair and store in resources/key as public.pem and private.pem using the following commands:
- openssl genrsa -out private.pem 2048
- openssl rsa -in private.pem -pubout -out public.pem
