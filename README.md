# Coding test

## Problem Statement


At `https://live-test-scores.herokuapp.com/scores` you'll find a service that follows the [Server-Sent Events](https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events) protocol. You can connect to the service using cURL:

        curl https://live-test-scores.herokuapp.com/scores

Periodically, you'll receive a JSON payload that represents a student's test score (a JavaScript number between 0 and 1), the exam number, and a student ID that uniquely identifies a student. For example:

        event: score
        data: {"exam": 3, "studentId": "foo", score: .991}

This represents that student foo received a score of `.991` on exam #3. 

Your job is to build an application that consumes this data, processes it, and stores it efficiently such that the data can be queried for various purposes. 

You may build this application in any language or stack that you prefer; we will use this project as part of your onsite interviews, so pick a language and tech stack with which you would be comfortable in a live coding session. You may use any open-source libraries or resources that you find helpful. **As part of the exercise, please replace this README file with instructions for building and running your project.** We will run your code as part of our review process.

Here are the purposes for which we might want to query the data and for which you should include SQL for reading from the data store:

1. List all users that have received at least one test score
2. List the test results for a specified student, and provides the student's average score across all exams
3. List all the exams that have been recorded
4. List all the results for the specified exam, and provides the average score across all students

Coding tests are often contrived, and this exercise is no exception. To the best of your ability, make your solution reflect the kind of code you'd want shipped to production. A few things we're specifically looking for:

* Well-structured, well-written, idiomatic, safe, performant code.
* Good data schema design. Whatever that means to you, make sure your implementation reflects it, and be able to defend your design.
* Ecosystem understanding. Your code should demonstrate that you understand whatever ecosystem you're coding against— including project layout and organization, use of third party libraries, and build tools.

That said, we'd like you to cut some corners so we can focus on certain aspects of the problem:

* You don't need to worry about the “ops” aspects of deploying your service — load balancing, high availability, deploying to a cloud provider, etc. won't be necessary.

That's it. Commit your solution to the provided GitHub repository (this one) and submit the solution using the Greenhouse link we emailed you. When you come in, we'll pair with you and walk through your solution and extend it in an interesting way.


## Solution by Abhishek

This repository contains a [Java 17](https://openjdk.org/projects/jdk/17/) based [SpringBoot](https://spring.io/guides/gs/spring-boot/) application as a solution to the problem statement above.

The solution creates a [Reactive Client](https://spring.io/reactive) to listen to the [Server Sent Events](https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events) coming from the specified URL, and writes to the database.

The database of choice in this solution is [MongoDb](https://github.com/mongodb/mongo) but the design of the solution allows it to be replaced by any database if it has [SpringBoot Repository](https://spring.io/projects/spring-data-jpa) support.

### Prerequisites

* [Docker daemon](https://docs.docker.com/config/daemon/start/) in Linux or [Docker Desktop](https://www.docker.com/products/docker-desktop/) on MacOS.
* [Java OpenJDK-17](https://openjdk.org/projects/jdk/17/)
* [Maven 3.8+](https://maven.apache.org/download.cgi)

### How to run the application

* Create docker containers for [MongoDb](https://hub.docker.com/_/mongo/) and [MongoDb-Express](https://hub.docker.com/_/mongo-express/). (Note that MongoDb-Express is optional, and can be replaced with [MongoDb Compass](https://www.mongodb.com/products/compass)).
    * Create docker network for mongo containers.

            docker network create mongo-network
    
    * Run mongodb container

            docker run -d -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=password --name mongodb --net mongo-network mongo
    
    * (Optional) Run mongodb-express container
        
            docker run -d -p 8081:8081 -e ME_CONFIG_MONGODB_ADMINUSERNAME=admin -e ME_CONFIG_MONGODB_ADMINPASSWORD=password --net mongo-network --name mongo-express -e ME_CONFIG_MONGODB_SERVER=mongodb mongo-express

    * Make sure the username and password are exactly same as mentioned in the command as this is same config used in the data configuration in the application too.

* Run Maven commands to test and run the application.
    * Build and run tests
            
            mvn test
    
    * Build and run application
    
            mvn spring-boot:run

* Use http://localhost:27017 with the given username and password from above command to connect to MongoDb to validate the updates in the table using the tool of your choice - MongoDb Compass and MongoDb-Express.
    * Lookup the Collections **exams**, **score** and **students** in **local** database to see the new records getting added.

### Strategy and Architecture

* To listen to a [Server Sent Events](https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events), I created a [Reactive Java Flux](https://spring.io/reactive) based client so that we can support variable traffic load.
* To create database, I utilized DockerHub's MongoDb container to create a container running MongoDb with port forwarded to standard MongoDb port 27017 in localhost.
* To access MongoDb Collections, I am using another container called MongoDb-Express which exposes an endpoint (http://localhost:8081). Another alternative to this will be to use MongoDb Compass Community tool to connect to MongoDb. This tool will be needed to run queries on top of MongoDb.
* As per the queries in the above list, my database collections and indexes were designed to get all the results in most optimized way.
    * Created three collections - **exams**, **scores** and **students**.
    * **Exam** records contain average score for each student in that exam. This value is updated as we read more and more data.
    * **Student** records contain average score for each subject scored by that student. This value is updated as we read more and more data.
    * The query to get average score on each exam or scored by each student becomes faster.
    * The **Score** record is stored as it is but two fields **examId** and **studentId** are also **Indexed**. This helps in faster query to get all results for specified exam or specified student.

### Database Collections and Schema

Some example collection schemas and sample data are shown below.

1. Score Collection and Document examples:


| _id (MongoDb Generated)     | examId  |  studentId  |   score   |
| ---------------------------:|:-------:|:-----------:|:----------|
| 63e0952fdffb7b628854b10a    | 4099    | "Rylee1"    | 0.7642121 |
| 63e0952fdffb7b628854b10b    | 4099    | "Wallac10"  | 0.5412312 |
| 63e0952fdffb7b628854b10c    | 4100    | "Rylee1"    | 0.9831234 |



2. Student Collection and Document examples:


| _id         |  averageScore  | numberOfExams |
| ------------|:--------------:|:--------------|
| "Rylee1"    |  0.87366775    |   2           |
| "Wallac10"  |  0.5412312     |   1           |



3. Exam Collection and Document examples


| _id   |  averageScore  | numberOfStudents |
| ------|:--------------:|:-----------------|
| 4099  |  0.65272165    |   2              |
| 4100  |  0.9831234     |   1              |


### Queries

Note that we are using mongodb and the application to access the collections and documents will use mongo query format and not SQL.
I am adding the corresponding equivalent SQL queries too.

In mongo shell or compass tool or mongo-express server, switch to **local** database.

1. List all users that have received at least one test score
    * Mongo filter query

            db.students.find()

    * SQL equivalent to get all users that have received at least one test score

            SELECT * FROM students;

2. List the test results for a specified student, and provides the student's average score across all exams
    * Mongo filter query for all results for a specified student

            db.scores.find({studentId: "value_of_student_id"})

    * SQL equivalent to get all results for a specifed student

            SELECT examId, studentId, score FROM scores WHERE studentId = "value_of_student_id";

    * Mongo filter query for a student's average score across all exams. Note we are calculating and storing the average in the student document as we read data.

            db.students.find(({_id: "value_of_student_id"})

    * SQL equivalent to get student's average score across all exams.

            SELECT averageScore FROM students WHERE _id = "value_of_student_id";

3. List all the exams that have been recorded
    * Mongo filter query

            db.exams.find()

    * SQL equivalent to get all exams

            SELECT * FROM exams;


4. List all the results for the specified exam, and provides the average score across all students
    * Mongo filter query

            db.scores.find({examId: value_of_exam_id})

    * SQL equivalent to get all result for a specified exam

            SELECT examId, studentId, score FROM scores WHERE examId = value_of_exam_id;

    * Mongo filter query for average score of the exam across all students. Note we are calculating and storing the average score in the exam document as we read data.

            db.exams.find({_id: value_of_exam_id})

    * SQL equivalent to get average score of an exam across all students.

            SELECT averageScore FROM exams WHERE _id = value_of_exam_id;


