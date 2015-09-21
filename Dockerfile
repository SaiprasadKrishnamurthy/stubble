FROM java:8
VOLUME /tmp
ADD target/stubble-jar-with-dependencies.jar stubble.jar
RUN bash -c 'touch /stubble.jar'
ENTRYPOINT ["java","-jar","/stubble.jar"]