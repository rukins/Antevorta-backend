FROM openjdk:17
ADD 'target/InventoryManagementSystem-1.0.jar' 'InventoryManagementSystem-docker-1.0.jar'

ENTRYPOINT ["java", "-jar","InventoryManagementSystem-docker-1.0.jar"]

EXPOSE 8081