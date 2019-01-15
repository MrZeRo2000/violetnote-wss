violetnote-ws.xml
needs to be placed to tomcat /conf/{engine}/{host} folder

logging.properties:
comment out all lines with log

server.xml:
comment out localhost_access_log configuration

web.xml:
added CORS filter
<filter>
  <filter-name>CorsFilter</filter-name>
  <filter-class>org.apache.catalina.filters.CorsFilter</filter-class>
</filter>
<filter-mapping>
  <filter-name>CorsFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
