#!/bin/bash

# script to set up JTA datasource in Wildfly image
# using jboss-cli

echo "=> Executing Customization script"

JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"standalone"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE.xml"}

function wait_for_server() {
  until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}

echo "========> Starting WildFly server"

echo "JBOSS_HOME  : " $JBOSS_HOME
echo "JBOSS_CLI   : " $JBOSS_CLI
echo "JBOSS_MODE  : " $JBOSS_MODE
echo "JBOSS_CONFIG: " $JBOSS_CONFIG
echo "=========> POSTGRESQL_URI (docker with networking): " $POSTGRES_URI

echo $JBOSS_HOME/bin/$JBOSS_MODE.sh -b 0.0.0.0 -c $JBOSS_CONFIG &
$JBOSS_HOME/bin/$JBOSS_MODE.sh -b 0.0.0.0 -c $JBOSS_CONFIG &

echo "==========> Waiting Wildfly to start..."
wait_for_server

echo "==========> Configuring the datasource..."

$JBOSS_CLI -c << EOF
# Criando o modulo PostgreSQL no wildfly
module add --name=org.postgresql --resources=/opt/jboss/wildfly/customization/postgresql-42.2.12.jar --dependencies=javax.api,javax.transaction.api

# Adicionando o driver do PostgreSQL
/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgresql",driver-class-name=org.postgresql.Driver)

data-source add --jndi-name=java:jboss/datasources/backend_DS --name=backend_DS --connection-url=jdbc:postgresql://$POSTGRES_URI/pessoaDB --driver-name=postgres --user-name=postgres --password=postgres --enabled=true

EOF

# Deploy app .war file to wildfly
cp /opt/jboss/wildfly/customization/java-postgres-be.war $JBOSS_HOME/$JBOSS_MODE/deployments/java-postgres-be.war

echo "=> Datasource done!"
if [ "$JBOSS_MODE" = "standalone" ]; then
  $JBOSS_CLI -c ":shutdown"
else
  $JBOSS_CLI -c "/host=*:shutdown"
fi

echo "=> Restarting WildFly..."
$JBOSS_HOME/bin/$JBOSS_MODE.sh -b 0.0.0.0 -c $JBOSS_CONFIG