@ECHO OFF

javac -classpath .;json.jar.;C:\tomcat\lib\json.jar.;C:\tomcat\lib\ojbdc8.jar Activity.java
javac -classpath .;json.jar.;C:\tomcat\lib\json.jar.;C:\tomcat\lib\ojbdc8.jar UploadClient.java
java -classpath .;json.jar Activity