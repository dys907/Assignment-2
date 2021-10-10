import java.io.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

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
         baos.write(requestString.getBytes());
         //Parse request
         ParsedRequest parsedRequest = new ParsedRequest(requestString);
         String caption = "";
         String date = "";
         String imgName = "";
         String file = "";
         System.out.println("just before parts call");

         for(Part part: parsedRequest.getParts()) {
            System.out.println("**************Parts of parsedRequest**************");
            System.out.println(part.getHeader());
            System.out.println(part.getContent());
            String header = part.getHeader().replace("\"", "");
            if (header.equals("caption")) {
               caption = part.getContent();
            }
            else if (header.equals("date")) {
               date = part.getContent();
            }
            else {
               imgName = header;
               file = part.getContent();
               System.out.println(file);
            }
         }
         System.out.println("Just before file creation");
         String extension = imgName.substring(imgName.lastIndexOf(".") + 1);
         String newFileName = imgName + "_" + date + "_" + caption + "." + extension;
         System.out.println("file length sanity check: " + file.length());


         OutputStream newImg = new BufferedOutputStream(new FileOutputStream("..\\..\\images\\" + newFileName));
         byte[] imgBytes = Base64.getDecoder().decode(file);

         newImg.write(imgBytes);


         //End Parse



         Clock clock = Clock.systemDefaultZone();
         long milliSeconds = clock.millis();
         //Writes the request info directly into a file
         OutputStream outputStream = new FileOutputStream(new File("..\\..\\images\\" + String.valueOf(milliSeconds) + ".png"));
         baos.writeTo(outputStream);
         outputStream.close();

//         PrintWriter out = new PrintWriter(response.getOutputstream(), true);

         //Pushes file names into servletBaos to get sent to output stream

         File dir = new File("..\\..\\images\\");
         String[] chld = dir.list();
//         for (int i = 0; i < chld.length; i++) {
//            String fileName = chld[i];
//            servletBaos.write((fileName + "\n").getBytes());
//         }
//         String jsonObj = "{" + "\n" + "fileNames";
//         JSONObject jsonObject = new JSONObject();
//         ArrayList<JSONObject> arr = new ArrayList<>();
//         for (String fileName : chld) {
//            JSONObject obj = new JSONObject().put("fileName", fileName);
//            arr.add(obj);
//         }
//         jsonObject.put("fileNames", arr.toArray());
//         servletBaos.write(jsonObject.toString().getBytes());
         boolean isBrowser = true; // change this to user agent
         if (isBrowser) {
            servletBaos.write(getListing("..\\..\\images\\").getBytes());
         } else {
            String jsonObj = "{\"filenames\":[";
            for (String fileName : chld) {
               jsonObj += "{\"fileName\":\"" + fileName + "\"},";
            }
            servletBaos.write(jsonObj.getBytes());
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

   private String getListing(String path) {

      String dirList =  null;

      File dir = new File(path);

      String[] chld = dir.list();

      for (String string : chld) {

         if ((new File(path + string)).isDirectory())

            dirList += "<li><button type=\"button\">" + string + "</button></li>";

         else

            dirList += "<li>" + string + "</li>";

      }

      return dirList;

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