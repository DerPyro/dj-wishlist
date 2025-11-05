# dj-wishlist

A program for DJs to easily manage music requests

[![codecov](https://codecov.io/github/DerPyro/dj-wishlist/graph/badge.svg?token=126XEV9W9H)](https://codecov.io/github/DerPyro/dj-wishlist)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Buy me a coffee](https://badgen.net/badge/icon/Buy%20me%20a%20coffee?icon=buymeacoffee&label)](https://buymeacoffee.com/derpyro)

## How to run

### from code

```bash
mvn clean spring-boot:run
```

or build and run with docker:

```bash
mvn clean package spring-boot:repackage
docker build -t fehuworks/dj-wishlist .
docker run -p 8080:8080 fehuworks/dj-wishlist
```

### from prebuilt Docker Image

```bash
docker pull fehuworks/dj-wishlist
docker run -p 8080:8080 fehuworks/dj-wishlist
```

## Changelog

### Version 0.0.2 ![Work in Progress](https://img.shields.io/badge/work%20in%20progress-orange?logo=construction&logoColor=white)

- Added Dockerfile for easy deployment
- Improved admin interface
- Fixed minor bugs
- Updated dependencies
- Improved documentation

### [Version 0.0.1 - 2025-10-29](https://github.com/DerPyro/dj-wishlist/releases/tag/0.0.1) [![Get 0.0.1 on Dockerhub](https://badgen.net/badge/icon/Image?icon=docker&label)](https://hub.docker.com/layers/fehuworks/dj-wishlist/0.0.1/images/sha256-90fb28e4b871bf9daf44b45caa7cc877fa92bfe56dc6d92b7c8eacf68549d96f) [![Get 0.0.1 on Github](https://badgen.net/badge/icon/Code?color=orange&icon=github&label)](https://github.com/DerPyro/dj-wishlist/releases/tag/0.0.1)

[![](https://badgen.net/badge/icon/Built%20on%20Java%2017?color=red&icon=java&label)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

- Initial release
- Basic functionality to add and vote for song wishes
- Simple web interface
- Rudimentary rights management system

## How to use it

Open your browser and go to [`http://localhost:8080`](http://localhost:8080)
You will see a simple interface where you can add songs to the wishlist and vote for them.
You can also see the current wishlist and the top voted songs.

There is a rudimentary rights management system in place.
When you add a Song-Wish you become the owner of that song-wish and can delete it.
Other users can only vote for it.
As admin, you can delete any song-wish.

If you want to log in as admin, use the following default credentials:

- Username: **admin**
- Password: **password123**
- Url: [`http://localhost:8080/admin`](http://localhost:8080/admin)

All rights are bound to the sessionId.
Only one session can be admin at a time.

## Configuration

If you are not happy with the default values you can adjust these value:

| Short description  | Default value | Environment variable name | Hints |
|--------------------|---------------|---------------------------|-------|
| Username for admin | admin         | ADMIN_USERNAME            |       |
| Password for admin | password123   | ADMIN_PASSWORD            |       |

### How do I use adjusted values?

Instead of running

```bash
docker run -p 8080:8080 fehuworks/dj-wishlist:0.0.2
```

you can simply add adjusted values as key-value pairs with the parameter **-e**:

```bash
docker run -p 8080:8080 -e ADMIN_USERNAME='much' -e ADMIN_PASSWORD='secure' fehuworks/dj-wishlist:0.0.2
```

### How do I know it worked?

Just look at the logs:

```text
...
2025-10-30T14:24:42.884Z  INFO 1 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 556 ms
2025-10-30T14:24:42.920Z  INFO 1 --- [           main] e.f.d.configuration.PropertyProvider     : Using 'admin' as admin username
2025-10-30T14:24:42.922Z  INFO 1 --- [           main] e.f.d.configuration.PropertyProvider     : Using 'password123' as admin password
2025-10-30T14:24:43.134Z  INFO 1 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
...
```

## Code Coverage in Detail

[![codecov](https://codecov.io/github/DerPyro/dj-wishlist/graphs/sunburst.svg?token=126XEV9W9H)](https://codecov.io/github/DerPyro/dj-wishlist)

## Code Quality in Detail

[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=bugs)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=coverage)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=DerPyro_dj-wishlist&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=DerPyro_dj-wishlist)
