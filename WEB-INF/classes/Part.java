import java.util.List;
import java.util.Arrays;
import java.util.Hashtable;

public class Part {
    private Hashtable<String,String> headers;
    private String content;
    public Part(String headers, String content) {
        List<String> headerList = Arrays.asList(headers.split("\\s*;\\s*"));
        for(String headerStr : headerList) {
            if(headerStr.contains("name=")) {
                int keyAssign = headerStr.indexOf("name=");
            }
        }
        this.content = content;
    }

}
