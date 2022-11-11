FROM openjdk:17
ADD 'target/Antevorta-backend-1.0.jar' 'Antevorta-backend-1.0.jar'

ENTRYPOINT ["java", "-jar","Antevorta-backend-1.0.jar"]

EXPOSE 8081