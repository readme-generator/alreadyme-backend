# alreadyme Java REST Service

A webservice that reads a readme file and resofres it as an [[ReadMeAttachment](https://github.com/readme-generator/readme-attachment)](https://github.com/readme-generator/readme-attachment) (downloadable as a pull request on GitHub).

## Getting Started

If you are starting from the repository repo=/path/to/repositories, you can simply clone the repository and run the following command to start the service:

    java -jar alreadyme-backend.jar

The service is not available in the browser as it has to be served by the application implementation. For this reason the service must be run below the root of your web application.

### How to deploy the services in the LeetCode framework?

We suggest you look at the [LeetCode](https://leetcode.com/) documentation that describes how to add a new component in a LeetCode project. You can use the Webpack Maven Plugin to either deploy the code automatically and create a new artifact, or you can use Maven to manually deploy:

    1. Create a new folder with the same name as your project and bundle the package

    2. Install the package dependency:

    mvn install --group=your-group --dependency=your_deps.txt

The package dependency requires the maven dependency plugin to work (for example, to install the dependencies).

### Getting Started

To run the service in production mode, add the service to your spring boot project's dependencies, and then create a service to handle the request:

    <dependency>
        <groupId>kr.markdown.alreadyme</groupId>
        <artifactId>alreadyme-server</artifactId>
        <!-- Required by Spring Boot -->
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

As both the external and spring boot project must have a web address( http://localhost:8080/api/v1/ai), you will need to change @RestController to @RestController("api/v1/ai") when making the service available in the web application.

### Install

If you are using the LeetCode docs, you can run the following command in your IDE to list the available plugins:

    mvn clean package

To install the plugin, run:

    mvn install:userinstall

You can add the plugin's dependency to the Maven dependency manager:

    mvn install:userinstall -DgroupId=<JOURNEY_GROUP_ID> -DartifactId=<SOURCE_ARTIFACT_ID> -Dversion=<VERSION>

However, it is recommended to create a new pom.xml file to make the plugin available on Gradle's build system:

    <plugin>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-maven-plugin</artifactId>
          <!-- Required by Spring Boot -->
          <version>1.5.4.RELEASE</version>
          <!-- Go to https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-maven-plugin/ for further information -->
							<!-- Static Package path -->
      <configuration>
        <staticPackage>${project.artifactId}</staticPackage>
      </configuration>

  </plugin>

## Usage

Forging a file and uploading it to the service.

```
curl -X POST --header "Accept: */*" -X POST -H "Content-Type: application/zip" \
                -X PUT /api/v1/ai/duggestion.zip \
                >/destination.zip
```

For determining the state of a contact list.

```
curl -X GET /api/v1/ai/contact