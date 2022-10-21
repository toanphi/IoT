# iot_admin_backend

# Note 
- Run steps:
- Backend -> Frontend -> MqttReceiver

# Requirement for installation
- java8
- maven (version > 3.5)
- mariadb 10.4
- create tables with commands specified in each entity: \src\main\java\com\uet\iot\database\entity
# Installation
- run mvn spring-boot:run in project base directory
- or build jar file with mvn clean install, a file named ...0.0.1-SNAPSHOT.jar will appear in /target folder. Run file with java -jar <filename>
