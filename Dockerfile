FROM gradle:4.0-alpine
ADD . .
RUN gradle clean build
