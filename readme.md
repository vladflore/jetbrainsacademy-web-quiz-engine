
### JetBrains Academy / Hyperskill Project - Web Quiz Engine

Features:
* register a user (basic authentication)
* create a quiz
* delete a quiz
* get all quizzes (with pagination and sort support)
* get a quiz by id
* solve a quiz
* get all solved quizzes (with pagination and sort support)

All the operations, except register a user, are protected by basic authentication.

`curl` example for calling different endpoints:

```
curl --user flore.vlad@gmail.com:welcome1  localhost:8889/api/quizzes -H 'Content-Type:application/json' | jq

curl -d "@register_user.json" -X POST localhost:8889/api/register -H 'Content-Type:application/json' | jq

curl -d "@create_quiz.json" --user flore.vlad@gmail.com:welcome1 -X POST localhost:8889/api/quizzes -H 'Content-Type:application/json' | jq

curl -d "@solve_quiz.json" --user flore.vlad@gmail.com:welcome1 -X POST localhost:8889/api/quizzes/2/solve -H 'Content-Type:application/json' | jq

curl --user flore.vlad@gmail.com:welcome1  localhost:8889/api/quizzes/completed -H 'Content-Type:application/json' | jq
```

...and yes this project has no tests :D (which is bad, I know, so don't do it like me, you should always write tests, even do TDD)

Connect to H2 from the command line:
```
java -cp /home/vlad/.gradle/caches/modules-2/files-2.1/com.h2database/h2/1.4.200/f7533fe7cb8e99c87a43d325a77b4b678ad9031a/h2-1.4.200.jar org.h2.tools.Shell -url \
    "jdbc:h2:file:/home/vlad/vladflore.dev/sandbox/jetbrains-academy-project/Web Quiz Engine/Web Quiz Engine/task/quizdb;ifexists=true" -user "sa" -password "password"

```

... then you can execute queries like:

`show tables;`

`select * from quiz;`
