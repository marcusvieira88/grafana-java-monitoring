# Introduction

This is a Grafana monitoring example, that was populated using a GraphQl API implemented in Java.

## For prepare the Grafana, Graphite and StatsD environments, please run:
```
docker-compose up
```

## Install Java Project

```
mvn install
```

## Start Java Project

```
mvn jetty:run
```
## For execute the Graphql queries:

```
http://localhost:8080/
```

## Api

### Graphql URL

http://localhost:8080/graphql-api

#### Mutations

<p>createUser</p>
<p>createMessage</p>

#### Queries Objects

**User**
<p>id</p>
<p>name</p>
<p>email</p>

**Message**
<p>id</p>
<p>createdAt</p>
<p>message</p>
<p>userId</p>
<p>user : User</p>



