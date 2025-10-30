# dj-wishlist
A program for DJs to easily manage music requests

## How to run

```bash
mvn clean package spring-boot:repackage
docker build -t fehuworks/dj-wishlist:0.0.2 .
docker run -p 8080:8080 fehuworks/dj-wishlist:0.0.2
```

## How to push to Docker Hub

```bash
docker login
docker push fehuworks/dj-wishlist:0.0.2
```

## Get it from Dockerhub

You can get it [here](https://hub.docker.com/r/fehuworks/dj-wishlist)

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