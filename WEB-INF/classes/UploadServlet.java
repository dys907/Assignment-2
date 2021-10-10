import java.io.*;
import java.time.Clock;
public class UploadServlet extends HttpServlet {
   private ByteArrayOutputStream servletBaos;

   public UploadServlet() {
      servletBaos = new ByteArrayOutputStream();
   }

   public void doPost(HttpRequest request, HttpResponse response) {
      try {
         InputStream in = request.getInputStream();   
         ByteArrayOutputStream baos = new ByteArrayOutputStream();  
         byte[] content = new byte[1];
         int bytesRead = -1;      
         while( ( bytesRead = in.read( content ) ) != -1 ) {  
            baos.write( content, 0, bytesRead );  
         }
         Clock clock = Clock.systemDefaultZone();
         long milliSeconds=clock.millis();
         OutputStream outputStream = new FileOutputStream(new File("..\\..\\images\\" + String.valueOf(milliSeconds) + ".png"));
         baos.writeTo(outputStream);
         outputStream.close();
         PrintWriter out = new PrintWriter(response.getOutputstream(), true);
         File dir = new File("..\\..\\images\\");
         String[] chld = dir.list();
      	 for(int i = 0; i < chld.length; i++){
            String fileName = chld[i];
            out.println(fileName+"\n");
            System.out.println(fileName);
         }
      } catch(Exception ex) {
         System.err.println(ex);
      }
   }
//
//   public void doGet(HttpRequest request, HttpResponse response) {
//      try {
//         Part filePart = request.getPart("fileName");
//         String captionName = request.getParameter("caption");
//         String formDate = request.getParameter("date");
//         String fileName = filePart.getSubmittedFileName();
//
//         if(fileName.equals("")){
//            response.setStatus(302);
//            response.sendRedirect("upload");
//            return;
//         }
//
//         if(formDate.equals("")) formDate = "2020-10-10";
//         if(captionName.equals("")) captionName = "No caption";
//         filePart.write(System.getProperty("catalina.base") + "/webapps/upload/images/" + fileName);
//
//         DataOutputStream out = new DataOutputStream(response);
//
//         response.setContentType("text/html");
//         PrintWriter out = response.getWriter();
//         String topPart = "<!DOCTYPE html><html><body><ul>";
//         String bottomPart = "</ul></body></html>";
//         out.println(topPart+getListing("C:\\tomcat\\webapps\\upload\\images")+bottomPart);
//         private String getListing(String path) {
//            String dirList =  null;
//            File dir = new File(path);
//            String[] chld = dir.list();
//            for(int i = 0; i < chld.length; i++){
//               if ((new File(path+chld[i])).isDirectory())
//                  dirList += "<li><button type=\"button\">"+chld[i]+"</button></li>";
//               else
//                  dirList += "<li>"+chld[i]+"</li>";
//            }
////            return dirList;
//         }
//      } catch() {
//
//      }
//   }

   public ByteArrayOutputStream getServletBaos() {
      return servletBaos;
   }
}