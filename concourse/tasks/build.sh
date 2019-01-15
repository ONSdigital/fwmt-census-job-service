#!/bin/sh
set -e
fwmt-census-job-service/gradlew --build-file fwmt-census-job-service/build.gradle build --stacktrace
cp fwmt-census-job-service/build/libs/*.jar build-output
cd fwmt-census-job-service/build/libs
ls
