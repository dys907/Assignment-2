import java.net.*;
import java.io.*;
import java.time.Clock;
public class UploadServerThread extends Thread {
   private Socket socket = null;
   private Class<?> theClass;
   public UploadServerThread(Socket socket) {
      super("DirServerThread");
      this.socket = socket;
   }
   public void run() {
      try {
         InputStream in = socket.getInputStream(); 
         HttpRequest req = new HttpRequest(in);
         OutputStream baos = new ByteArrayOutputStream(); 
         HttpResponse res = new HttpResponse(baos);

         theClass = Class.forName("");
         UploadServlet myUploadServlet = (UploadServlet)theClass.newInstance();
         UploadServlet httpServlet = myUploadServlet;//new UploadServlet();
         (httpServlet).requestHandler(req, res);

         ByteArrayOutputStream uploadServletResponse = (httpServlet).getServletBaos();
         baos.write(uploadServletResponse.toByteArray());
         OutputStream out = socket.getOutputStream(); 
         out.write(((ByteArrayOutputStream) baos).toByteArray());
         socket.close();
      } catch (Exception e) { e.printStackTrace(); }
   }
}
