import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import org.json.JSONException;
import org.json.JSONObject;

public class HitServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        String topPart = "<!DOCTYPE html><html><body><ul>";

        String bottomPart = "</ul></body></html>";

        out.println(topPart+getListing("C:\\")+request.getParameter("path")+bottomPart);

    }

    /**
     * Gets the listing from the returned JSON Object
     * @param path
     * @return formated buttons
     */
    private String getListing(JSONObject object) {

        String returnStr = "";
        
        //object

       return returnStr; 

    }

    /**
     * Gets the listing from a hardcoded path
     * @param path
     * @return formated buttons
     */
    private String getListing(String path) {

        String dirList =  null;
        File dir = new File(path);
        String[] chld = dir.list();

        for(int i = 0; i < chld.length; i++){

            if ((new File(path+chld[i])).isDirectory())

                dirList += "<li><button type=\"button\">"+chld[i]+"</button></li>";

            else

                dirList += "<li>"+chld[i]+"</li>";

        }

        return dirList;

    }

}


