import java.io.*;
import java.net.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadClient {

    private String rawFileName;
    private String rawDate;
    private String rawCaption;
    private String fileName;
    private String boundary = "boundary";

    /**
     * UploadClient Default Conistructor
     */
    public UploadClient() {
        fileName = "empty";
    }

    /**
     * Upload Client Constructor
     * @param fileString
     */
    public UploadClient(String fileString) throws IncorrectFileTypeException {
        fileName = fileString;

        String[] brokenData = fileString.split("_");
        rawFileName = brokenData[0];
        rawDate = brokenData[1];
        rawCaption = brokenData[2];
        String fileExtension = "";
        int index = rawFileName.lastIndexOf('.');
        if (index > 0) fileExtension = rawFileName.substring(index + 1);
        if (!(fileExtension.matches("(?i)png|jpeg|gif"))) {
            throw new IncorrectFileTypeException("Can only upload png/jpeg/gif images");
        }
    }

    /**
     * Uploads a file to a server and gets a reply
     *  of the other files on the server
     * @return Listing of the files on the server
     */
    public String uploadFile() {
        
        String listing = "";
        String endMessage = "\r\n\r\n";

        try {
            Socket socket = new Socket("localhost", 8999);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream();
            FileInputStream fis = new FileInputStream("AndroidLogo.png");
            byte[] bytes = fis.readAllBytes();

            //Get the length of the message for the header
            int messageLength = getMessageBody(fileName, bytes).length();

            //Start the POST Message
            out.write(getHeaderMessage(messageLength).getBytes()); 
            
            //Write thr fileName and image as parameters in the header
            out.write(getMessageBody(fileName, bytes).getBytes());
            
            //End the POST
            out.write(endMessage.getBytes());

            //Shutdown the output to the server
            socket.shutdownOutput();
            fis.close();

            //Get the return from the server as a listing of all images
            System.out.println("Getting reply from server...\n");
            String filename = "";
            while ((filename = in.readLine()) != null) {
                listing += filename;
            }
            socket.shutdownInput();
        } catch (Exception e) {
            System.err.println(e);
        }
        return listing;
    }

    /**
     * Gets the Header Message
     * @param messageLen length of the image and filename
     * @return header as String 
     */
    public String getHeaderMessage(int messageLen) {

        StringBuilder builder = new StringBuilder();
        builder.append("POST /asn2/upload HTTP/1.0");
        builder.append("\r\n");

        builder.append("Host: localhost:8999");
        builder.append("\r\n");

        builder.append("Content-Type: multipart/form-data;boundary=\"" + boundary + "\"");
        builder.append("\r\n");

        builder.append("Content-Length: " + messageLen);
        builder.append("\r\n");
        
        return builder.toString();
    }

    /**
     * Gets the upload data 
     * @return data to go to upload stream
     */
    private String getMessageBody(String fileName, byte[] image) {
        return 
            "--" + boundary + "\n"
            + "Content-Disposition: form-data; name=\"fileName\"" + "\n"
            + "\n"
            + fileName + "\n"

            + "--" + boundary + "\n"
            + "Content-Disposition: form-data; name=\"image\"" + "\n"
            + "\n"
            + image.toString() + "\n"

            + "--" + boundary + "\n"
            + "Content-Disposition: form-data; name=\"rawFileName\"" + "\n"
            + "\n"
            + rawFileName + "\n"

            + "--" + boundary + "\n"
            + "Content-Disposition: form-data; name=\"rawDate\"" + "\n"
            + "\n"
            + rawDate + "\n"

            + "--" + boundary + "\n"
            + "Content-Disposition: form-data; name=\"rawCaption\"" + "\n"
            + "\n"
            + rawCaption + "\n"

            + "--" + boundary + "--";           
   }
}