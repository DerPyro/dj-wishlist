# dj-wishlist
A program for DJs to easily manage music requests

## How to run

```bash
mvn clean package spring-boot:repackage
docker build -t dj-wishlist .
docker run -p 8080:8080 dj-wishlist
```