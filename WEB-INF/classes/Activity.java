import java.io.*;
import java.util.Scanner;
public class Activity {

   private String fileName;
   private String dateCreated;
   private String caption;
   private String dirName = null; 
   
   /**
    * Drives the console application
    * @param args unused
    * @throws IOException
    */
   public static void main(String[] args) throws IOException {

      //Show a welcome message
      displayWelcomeMessage();

      //Create an input for the user
      Scanner userInput = new Scanner(System.in);

      //Get users fileName
      System.out.print("Please enter a file name: ");
      String fileName = userInput.nextLine();

      //Get users date
      System.out.print("Please enter a date: ");
      String date = userInput.nextLine();
      
      //Get users caption
      System.out.print("Please enter a caption: ");
      String caption = userInput.nextLine();

      //Close the Scanner
      userInput.close();

      new Activity(fileName, date, caption).onCreate();
   }

   /**
    * Activity constructor
    */
   public Activity(String fileName, String dateCreated, String caption) {
      this.fileName = fileName;
      this.dateCreated = dateCreated;
      this.caption = caption;
   }
   
   /**
   * Must be Called when Activity is ran 
   */
   public void onCreate() {
      System.out.println(new UploadClient(generateFileName(fileName, dateCreated, caption)).uploadFile());
   }

   /**
     * Generates a filename from params given
     * @param filename
     * @param dateCreated
     * @param keyword
     * @return finished filename
     */
   private String generateFileName(String filename, String dateCreated, String caption) {
      return (filename + "_" + dateCreated + "_" + caption).trim();
   }

   /**
    * Prints out a simple welcome message
    */
   public static void displayWelcomeMessage() {
      
      System.out.println("\n\n");

      System.out.println("=========================");
      System.out.println("|                       |");
      System.out.println("|   WELCOME TO UPLOAD   |");
      System.out.println("|         CLIENT        |");
      System.out.println("|                       |");
      System.out.println("=========================\n");
   }
}
