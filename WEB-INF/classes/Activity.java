import java.io.*;

public class Activity {

    private String fileName = null;
    private String dateCreated = null;
    private String keyword = null;

    public static void main(String[] args) throws IOException {

        new Activity(args[0], args[1], args[2]).onCreate();

    }

    public Activity(String filename, String dateCreated, String keyword) {

        this.fileName = generateFileName(filename, dateCreated, keyword);

        this.dateCreated = dateCreated;

        this.keyword = keyword;

        System.out.println("Got file");
        System.out.println(filename);
        System.out.println(dateCreated);
        System.out.println(keyword);
    }

    /**
     * Must be Called when Activity is ran 
     */
    public void onCreate() {
        System.out.println(new FileUploadServlet(this.fileName, this.dateCreated, this.keyword).uploadFile());
    }

    /**
     * Generates a filename from params given
     * @param filename
     * @param dateCreated
     * @param keyword
     * @return finished filename
     */
    private String generateFileName(String filename, String dateCreated, String keyword) {
        return (filename + "_" + dateCreated + "_" + keyword).trim() + ".png";
    }

}