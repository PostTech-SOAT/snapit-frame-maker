FROM maven:3.9.6-amazoncorretto-21-debian

WORKDIR /opt/app

ENV APP_HOME /opt/app

# Adicionando um usuário e suas permissões
RUN groupadd -r snapitgroup && useradd -r -g snapitgroup -d $APP_HOME -s /sbin/nologin snapituser

RUN chown -R snapituser:snapitgroup $APP_HOME

RUN chmod -R 755 $APP_HOME

#Copiando o pom.xml
COPY pom.xml .

#Copiando a aplicação
COPY src ./src

#Buildando a aplicação
RUN mvn package -DskipTests

#Expondo a porta 28081
EXPOSE 28081

#Executando a aplicação
ENTRYPOINT ["java", "-jar", "/opt/app/target/snapit-frame-maker-0.0.1-SNAPSHOT.jar", "-Xms2048M", "-Xmx3072M"]