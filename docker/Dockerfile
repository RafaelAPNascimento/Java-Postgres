FROM maven:3.6.3-openjdk-11-slim AS Builder
LABEL key="https://finalexception.blogspot.com"

COPY Java-Postgres/pom.xml /app/pom.xml
COPY Java-Postgres/src /app/src
WORKDIR /app
RUN mvn clean package

FROM jboss/wildfly
EXPOSE 8080
ADD Java-Postgres/docker/customization /opt/jboss/wildfly/customization
COPY --from=Builder /app/target/java-postgres-be.war /opt/jboss/wildfly/customization

CMD ["/opt/jboss/wildfly/customization/execute.sh"]