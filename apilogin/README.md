Cómo importar datos desde una base de datos H2
=======================================
1. Añadir las dependencias de JPA y H2 al archivo `pom.xml`.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Configurar la conexión a la base de datos H2 en el archivo `application.properties`.
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
```
3. Crear unos repositorios JPA para las entidades.
4. Crear dichas entidades JPA basadas en los modelos que utilizamos.
5. Crear unos mappers para convertir las entidades JPA a los modelos que utilizamos y viceversa.
6. Crear/modificar los servicios que utilicen los repositorios y mappers.
7. Crear un archivo `data.sql` en el directorio `src/main/resources` para inicializar la base de datos H2 con datos de ejemplo (las contraseñas deben estar hasheadas, con BCrypt la salt está ya incluida en la contraseña).
