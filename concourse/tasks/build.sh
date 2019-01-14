#!/bin/sh
set -e
fwmt-census-job-service/gradlew --build-file fwmt-census-job-service/build.gradle build
cp fwmt-census-job-service/build/libs/*.jar build-output
