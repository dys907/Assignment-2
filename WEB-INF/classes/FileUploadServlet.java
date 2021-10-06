import java.io.*;
import java.net.*;
import java.security.DrbgParameters.Capability;
import java.util.Date;

public class FileUploadServlet {

    private String fileName = null;
    private String dateCreated = null;
    private String keyword = null;

    public Socket socket = null;


    /**
     * FileUploadServlet Default Constructor
     */
    public FileUploadServlet() {

        fileName = "empty";
        dateCreated = new Date().toString();
        keyword = "";

        setSocket();
    }

    /**
     * FileUploadServlet Constructor
     * @param filename
     * @param date
     * @param keyword
     */
    public FileUploadServlet(String name, String date, String key) {
        
        this.fileName = name;
        this.dateCreated = date;
        this.keyword = key;

        setSocket();
    }

    /**
     * Sets the socket to upload to UploadServlet at localhost:8081
     */
    public void setSocket() {
        try {
            socket = new Socket("localhost", 8081);
        } catch (Exception e) {}
    }

    /**
     * Sets the socket to upload to UploadServlet
     * @param hostName host name ot set
     * @param portnum port num to set
     */
    public void setSocket(String hostName, int portnum) {
        try {
            socket = new Socket(hostName, portnum);
        } catch (Exception e) {}
    }

    /**
     * Uploads the file and returns the filename
     * @return
     */
    public String uploadFile() {
        
        System.out.println("Uploading");

        String uploadData = getUploadData();

        DataOutputStream out;
        String pikachu = "pikachu";

        try {

            out = new DataOutputStream(socket.getOutputStream());

            out.writeBytes("POST /midp/upload HTTP/1.0");
            out.writeBytes("\r\n");

            out.writeBytes("Host: localhost:8081");
            out.writeBytes("\r\n");

            // out.writeBytes("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            // out.writeBytes("\r\n");

            // out.writeBytes("Accept-Language: en-US,en;q=0.");
            // out.writeBytes("\r\n");

            out.writeBytes("Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryD9JJQuX4ZDbHlYQy");
            out.writeBytes("\r\n");

            out.writeBytes("Content-Length: " + uploadData.length());
            out.writeBytes("\r\n");

            out.writeBytes(uploadData);

            out.writeBytes("\r\n\r\n");
            out.close();


        } catch (IOException ioe) {
            System.out.println("Fail to upload: " + ioe);
        }

        return fileName;

    }

    /**
     * Gets the upload data 
     * @return data to go to upload stream
     */
    private String getUploadData() {
         return 
            "------WebKitFormBoundaryD9JJQuX4ZDbHlYQy\n"
            + "Content-Disposition: form-data; name=\"fileName\"\n"
            + "\n"
            + fileName + "\n\n"

            + "------WebKitFormBoundaryD9JJQuX4ZDbHlYQy\n"
            + "Content-Disposition: form-data; name=\"date\"\n"
            + "\n"
            + dateCreated + "\n\n"

            + "------WebKitFormBoundaryD9JJQuX4ZDbHlYQy\n"
            + "Content-Disposition: form-data; name=\"caption\"\n"
            + "\n"
            + keyword + "\n\n"
            
            + "------WebKitFormBoundaryD9JJQuX4ZDbHlYQy--\n";
            
    }

}
