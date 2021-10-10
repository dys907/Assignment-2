package aspects;
import aspects.IncorrectFileTypeException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.JoinPoint;

@Aspect
public class ErrorLogAspect {
 
	@Before("handler(*) && args(e)")
    public void logCaughtException(JoinPoint thisJoinPoint, Exception e) {
        System.out.println("Exception caught and handler");
        System.out.println(thisJoinPoint + " -> " + e);
       
        File myObj = new File("ErrorLog.txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter("ErrorLog.txt", true))) {
            writer.println("Error Encountered and Written to Log: -> " + e);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
    
}