import java.io.*;
import java.nio.charset.StandardCharsets;
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
         doPost(req, res);
      }
   }
   public void doPost(HttpRequest request, HttpResponse response) {
      try {
         //baos used to write to file
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         String inputLine;
         String requestString = "";
         while (br.ready() && (inputLine = br.readLine()) != null) {
//            inputLine = br.readLine();
            requestString += inputLine + "\n";
         }

//         System.out.println("==========REQ STRING=========");
//         System.out.println(requestString);

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
            if (header.equals("caption") || header.equals("rawCaption")) {
               caption = part.getContent();
               System.out.println("CAPTION");
               System.out.println(caption);
            }
            else if (header.equals("date") || header.equals("rawDate")) {
               date = part.getContent();
            }
            else if (header.equals("fileName")){
               imgName = part.getContent();
            } else if (header.equals("image")){
               file = part.getContent();
            } else {
               imgName = header;
               file = part.getContent();
            }
         }
         String extension = imgName.substring(imgName.lastIndexOf(".") + 1);
         if (!extension.equals("png") || !extension.equals("jpg")) extension = "png";
         System.out.println("Just before file creation");
         String newFileName = imgName + "_" + date + "_" + caption + "." + extension;
         System.out.println("------------------fileName: " + newFileName);
         System.out.println("file length sanity check: " + file.length());
         System.out.println(file);


         //FILE WRITING STARTS HERE
         if (parsedRequest.getBase64Encoded()) {
            System.out.println("hopefully this worked");

//            BufferedReader in = new BufferedReader(new StringReader(file));


            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = file.getBytes();


//            for (int length; (length = in.read(buffer)) != -1; ) {
//               result.write(buffer, 0, length);
//            }

            String rawData = result.toString(StandardCharsets.UTF_8.name());
            byte[] data = rawData.getBytes();


//            System.out.println(Arrays.toString(data));

            String imageString = Base64.getEncoder().withoutPadding().encodeToString(data);
            byte[] decodeImg = Base64.getDecoder().decode(imageString);

            OutputStream outputStream = new FileOutputStream(new File("..\\..\\images\\"+ newFileName));

//            outputStream.write(decodeImg, 0, decodeImg.length);
            outputStream.write(file.getBytes());
            outputStream.close();

            //NOT BASE ENCODED FILE WRITE HERE
         } else {
            OutputStream newImg = new BufferedOutputStream(new FileOutputStream("..\\..\\images\\" + newFileName));
//         byte[] imgBytes = Base64.getDecoder().decode(file);

            newImg.write(file.getBytes());
         }

//         PrintWriter out = new PrintWriter(response.getOutputstream(), true);


            File dir = new File("..\\..\\images\\");
            String[] chld = dir.list();
            Arrays.sort(chld);

            boolean isBrowser = !parsedRequest.getBase64Encoded(); // change this to user agent
            if (isBrowser) {
               String body = getListing("..\\..\\images\\");
               int bodyLength = body.length();
               String header = createGetHeader(bodyLength);

               servletBaos.write(header.getBytes());
               servletBaos.write(body.getBytes());
            } else {
               String jsonObj = "{\"fileNames\":[";
               for (String fileName : chld) {
                  jsonObj += "{\"fileName\":\"" + fileName + "\"},";
               }
               jsonObj = jsonObj.substring(0, jsonObj.length()-1);
               jsonObj += "]}";

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
      String dirList =  "<!DOCTYPE html>" +
              "<html><head><title>File Upload Results</title></head>" +
              "<body><ul>";
      File dir = new File(path);
      String[] child = dir.list();
      Arrays.sort(child);

      for (String string : child) {
         if ((new File(path + string)).isDirectory())
            dirList += "<li><button type=\"button\">" + string + "</button></li>";
         else
            dirList += "<li>" + string + "</li>";
      }
      dirList += "</ul></body></html>";
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