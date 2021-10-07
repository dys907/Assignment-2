@ECHO OFF

javac -classpath .;C:\tomcat\lib\ojbdc8.jar Activity.java
javac -classpath .;C:\tomcat\lib\ojbdc8.jar UploadClient.java

java Activity