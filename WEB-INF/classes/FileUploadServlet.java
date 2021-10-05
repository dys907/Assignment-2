import java.io.*;
import java.net.*;
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

        try {

            out = new DataOutputStream(socket.getOutputStream());

            out.writeBytes("POST /midp/upload HTTP/1.0\r\n\r\n");

            out.writeBytes(uploadData);
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
        return "DATABYTES_" + (fileName + "_" + dateCreated + "_" + keyword).trim();
    }

}
