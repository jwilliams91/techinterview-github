#!/bin/bash

mvn spring-boot:run -f ../GitHubApi/pom.xml &
MVN_PID=$!

killMvn() {
    trap SIGINT
    kill -9 $MVN_PID
}

trap "killMvn" INT
ng serve