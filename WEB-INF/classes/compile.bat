@ECHO OFF

javac -classpath .;C:\tomcat\lib\ojbdc8.jar.;C:\tomcat\lib\servlet-api.jar.;C:\tomcat\lib\cos.jar C:\tomcat\webapps\midp\WEB-INF\classes\UploadServlet.java

::c:\tomcat\bin\startup.bat

javac -classpath .;C:\tomcat\lib\ojbdc8.jar *.java
javac -classpath .;C:\tomcat\lib\ojbdc8.jar Activity.java
javac -classpath .;C:\tomcat\lib\ojbdc8.jar FileUploadServlet.java

java Activity "pikachu" "2021" "electric"