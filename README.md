# dj-wishlist
A program for DJs to easily manage music requests

## How to run

```bash
mvn clean package spring-boot:repackage
docker build -t fehuworks/dj-wishlist:0.0.1 .
docker run -p 8080:8080 fehuworks/dj-wishlist:0.0.1
```

## How to push to Docker Hub

```bash
docker login
docker push fehuworks/dj-wishlist:0.0.1
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

If you want to log in as admin, use the following credentials:
- Username: **admin**
- Password: **password123**
- Url: [`http://localhost:8080/admin`](http://localhost:8080/admin)

All rights are bound to the sessionId.
Only one session can be admin at a time.