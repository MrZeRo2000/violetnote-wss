
FROM tomcat:8.5

COPY config/tomcat/context.xml /usr/local/tomcat/webapps/manager/META-INF/
COPY config/tomcat/tomcat-users.xml /usr/local/tomcat/conf/

COPY config/tomcat/violetnote-wss.xml /usr/local/tomcat/conf/Catalina/localhost/

COPY build/libs/violetnote-wss.war /usr/local/tomcat/webapps/