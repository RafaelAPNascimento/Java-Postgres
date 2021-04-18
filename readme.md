subindo backend jax-rs, postgres, wildfly com docker compose...

developing...

Wildfly Datasource configuration:

To test locally, before containerizing it, you must add the following block to you local wildfly in standalone.xml file inside <datasources> tag 

```xml
<datasource jta="true" jndi-name="java:jboss/datasources/backend_DS" pool-name="backend_DS" enabled="true">
    <connection-url>jdbc:postgresql://localhost:5432/pessoaDB</connection-url>
        <driver-class>org.postgresql.Driver</driver-class>
            <driver>postgresql</driver>
            <pool>
                <min-pool-size>3</min-pool-size>
                <initial-pool-size>3</initial-pool-size>
            </pool>
            <security>
                <user-name>postgres</user-name>
                <password>postgres</password>
            </security>
</datasource>
<drivers>
    <driver name="postgresql" module="org.postgresql"/>
        <driver name="h2" module="com.h2database.h2">
        <xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
    </driver>           
</drivers>
```
also create a module org.postgresql in <wildfly-home>/modules/system/layers/base/org/postgresql/main/

put the postgre driver there and module.xml file:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.postgresql"> 
    <resources>
        <resource-root path="postgresql-42.2.12.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```