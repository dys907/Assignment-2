import java.io.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UploadServlet extends HttpServlet {
   private OutputStream servletBaos;
   private BufferedReader br;

   public UploadServlet() {
      servletBaos = new ByteArrayOutputStream();

   }

   public void requestHandler(HttpRequest req, HttpResponse res) throws IOException {
      br = new BufferedReader(new InputStreamReader(req.getInputStream()));
      String inputLine = br.readLine();;

      if (inputLine.contains("GET")) {
         doGet(req,res);

      }
      if (inputLine.contains("POST")) {
         System.out.println("IN RUN METHOD");
         doPost(req, res);
      }
   }
   public void doPost(HttpRequest request, HttpResponse response) {
      try {
         //baos used to write to file
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         String inputLine;
         String requestString = "";
         while (br.ready()) {
            inputLine = br.readLine();
            requestString += inputLine + "\n";
         }
         System.out.println("Original request String");
         System.out.println(requestString);
         System.out.println(" ");
         baos.write(requestString.getBytes());
         //Parse request
         ParsedRequest parsedRequest = new ParsedRequest(requestString);
         System.out.println("Header");
         System.out.println(parsedRequest.getHeaders());
         System.out.println("ContentType");
         System.out.println(parsedRequest.getContentType());
         System.out.println("Request");
         System.out.println(parsedRequest.getRequest());
         System.out.println("Body");
         System.out.println(parsedRequest.getBody());
         for(Part part: parsedRequest.getParts()) {
            System.out.println("Parts of parsedRequest");
            System.out.println(parsedRequest.getHeaders());
            System.out.println(part.getContent());
         }

         //End Parse

         Clock clock = Clock.systemDefaultZone();
         long milliSeconds = clock.millis();
         //Writes the request info directly into a file
         OutputStream outputStream = new FileOutputStream(new File("..\\..\\images\\" + String.valueOf(milliSeconds) + ".png"));
         baos.writeTo(outputStream);
         outputStream.close();

         PrintWriter out = new PrintWriter(response.getOutputstream(), true);

         //Pushes file names into servletBaos to get sent to output stream

         File dir = new File("..\\..\\images\\");
         String[] chld = dir.list();
         for (int i = 0; i < chld.length; i++) {
            String fileName = chld[i];
            servletBaos.write((fileName + "\n").getBytes());
         }
      } catch (Exception ex) {
         System.err.println(ex);
      }
   }

   public void doGet(HttpRequest request, HttpResponse response) {
      try {
         int messageLen = createGetMessageBody().length();
         String header = createGetHeader(messageLen);
         String body = createGetMessageBody();

         servletBaos.write(header.getBytes());
         servletBaos.write(body.getBytes());

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private String createGetHeader(int messageLen) {
      String endLine = "\r\n";
      SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
      String httpDate = sdf.format(new Date());

      StringBuilder builder = new StringBuilder();
      builder.append("HTTP/1.1 200 OK")
              .append(endLine);
      builder.append("Date: ").append(httpDate)
              .append(endLine);
      builder.append("Server: MyLaptop").append(httpDate)
              .append(endLine);
      builder.append("Connection: close")
              .append(endLine);
      builder.append("Content-Length: ").append(messageLen)
              .append(endLine);
      builder.append("Content-Type: text/html")
         .append(endLine)
         .append("\n");

      return builder.toString();
   }

   private String createGetMessageBody() {
      return
      "<!DOCTYPE html>" +
              "<html><head><title>File Upload Form</title></head>" +
              "<body><h1>Upload file</h1>" +
              "<form method=\"POST\" action=\"/\" " +
              "enctype=\"multipart/form-data\">" +
              "<input type=\"file\" name=\"fileName\"/><br/><br/>" +
              "Caption: <input type=\"text\" name=\"caption\"<br/><br/>" +
              "<br />" +
              "Date: <input type=\"date\" name=\"date\"<br/><br/>" +
              "<br />" +
              "<input type=\"submit\" value=\"Submit\"/>" +
              "</form>" +
              "</body></html>"
              + "\r\n\n";
   }

   public ByteArrayOutputStream getServletBaos() {
      return (ByteArrayOutputStream) servletBaos;
   }
}