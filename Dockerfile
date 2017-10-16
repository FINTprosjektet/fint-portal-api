FROM gradle:4.2.1-jdk8-alpine
#RUN apk update && apk add libstdc++ && rm -rf /var/cache/apk/*
USER root
ADD . .
RUN gradle --no-daemon clean build
