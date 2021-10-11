@ECHO OFF

javac FileUploadServlet.java
javac HitServlet.java
javac HttpRequest.java
javac HttpResponse.java
javac HttpServlet.java
javac IncorrectFileTypeException.java
javac ParsedRequest.java
javac Part.java
javac UploadServer.java
javac UploadServerThread.java
javac UploadServlet.java
javac -g -classpath .;json.jar.;aspectj\lib\aspectjrt.jar;aspectj\lib\aspectjweaver.jar aspects/ErrorLogAspect.java UploadServer.java
java -javaagent:aspectj\lib\aspectjweaver.jar -classpath .;aspectj\lib\aspectjrt.jar;aspectj\lib\aspectjweaver.jar;json.jar UploadServer