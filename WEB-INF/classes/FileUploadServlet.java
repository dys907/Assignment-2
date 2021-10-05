import java.io.*;
import java.util.Date;

public class FileUploadServlet {

    private String fileName = null;
    private String dateCreated = null;
    private String keyword = null;

    /**
     * FileUploadServlet Default Constructor
     */
    public FileUploadServlet() {

        fileName = "empty";
        dateCreated = new Date().toString();
        keyword = "";
        
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

    }

    /**
     * Uploads the file and returns the filename
     * @return
     */
    public String uploadFile() {
        
        System.out.println("Uploading");

        return fileName;

    }
}
