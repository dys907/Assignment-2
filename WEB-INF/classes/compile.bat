@ECHO OFF

javac -classpath .;C:\tomcat\lib\json.jar.;C:\tomcat\lib\ojbdc8.jar Activity.java
javac -classpath .;C:\tomcat\lib\json.jar.;C:\tomcat\lib\ojbdc8.jar UploadClient.java
javac -g -classpath .;json.jar.;aspectj\lib\aspectjrt.jar;aspectj\lib\aspectjweaver.jar aspects/ErrorLogAspect.java Activity.java UploadClient.java IncorrectFileTypeException.java
java -javaagent:aspectj\lib\aspectjweaver.jar -classpath .;aspectj\lib\aspectjrt.jar;aspectj\lib\aspectjweaver.jar;json.jar Activity