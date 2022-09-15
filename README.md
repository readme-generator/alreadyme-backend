# ALREADYME-Backend

![image](https://user-images.githubusercontent.com/72238126/190327489-31753722-2449-4889-9544-a811acb5577f.png)
## What is the ALREADYME.md?
ALREADYME is a multiplatform desktop application which create a README.md. If you input your github repository, it will analyze source codes of the repository and create README.md using AI. Let's create README.md of repository with only one-click.

---

<br>

## Development Environment
- Java @17.x
- MySQL @8.x
- Spring Boot @2.7.2
- Spring Data JPA
- Spring Cloud for AWS
- commons-io @2.6
- httpclient @4.5.13

---

<br>

## How to run server?
Before starting the server, Create `application-dev.properties` file in src/main/resources path and use that template.

``` properties

# dev profiles

# MySQL Connection
spring.datasource.driver-class-name=
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

# JPA Setting
spring.jpa.database=
spring.jpa.database-platform=
spring.jpa.show-sql=
spring.jpa.hibernate.ddl-auto=
spring.jpa.properties.hibernate.format_sql=
spring.jpa.open-in-view=

# GITHUB Token
github.id=
github.token=
github.name=
github.email=

# AWS
cloud.aws.credentials.access-key=
cloud.aws.credentials.secret-key=
cloud.aws.region.static=
cloud.aws.s3.bucket=
cloud.aws.stack.auto=

# Ai-Server
ai-server.host=

```

<br>

## Repository

### ALREADYME.md Backend

[ALREADYME.md Backend](https://github.com/readme-generator/alreadyme-backend)

<br>

### ALREADYME.md Desktop

[ALREADYME.md Desktop](https://github.com/readme-generator/alreadyme-desktop)

<br>

### ALREADYME.md AI

[ALREADYME.md AI](https://github.com/readme-generator/alreadyme-ai-research)

---

<br>

## License

[Apache LICENSE-2.0](https://github.com/readme-generator/alreadyme-backend/blob/develop/LICENCE)

---
