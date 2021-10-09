import java.net.*;
import java.io.*;
import java.time.Clock;
public class UploadServerThread extends Thread {
   private Socket socket = null;
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

         DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
         BufferedReader br = new BufferedReader(
                 new InputStreamReader(in));
         String inputLine;
         inputLine = br.readLine();
         HttpServlet httpServlet = new UploadServlet();


         if (inputLine.contains("GET")) {
            httpServlet.doGet(req,res);

         }
         if (inputLine.contains("POST")) {
            httpServlet.doPost(req, res);
         }

         ByteArrayOutputStream uploadServletResponse = ((UploadServlet) httpServlet).getServletBaos();
         baos.write(uploadServletResponse.toByteArray());
         OutputStream out = socket.getOutputStream(); 
         out.write(((ByteArrayOutputStream) baos).toByteArray());
         socket.close();
      } catch (Exception e) { e.printStackTrace(); }
   }
}
