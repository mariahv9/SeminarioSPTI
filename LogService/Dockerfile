FROM openjdk:8

WORKDIR /usrapp/bin

ENV PORT 6010

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency

CMD ["java","-cp","./classes:./dependency/*","edu.escuelaing.spti.App"]