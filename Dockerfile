FROM openjdk:17-alpine

COPY build/libs/*.jar fileDiff.jar

ENTRYPOINT java -jar ${SYS_PROPS} fileDiff.jar
