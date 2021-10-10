import java.util.List;
import java.util.Arrays;
import java.util.Hashtable;

public class Part {
    private String header;
    private String content;
    public Part(String headers, String content) {
        List<String> headerList = Arrays.asList(headers.split("\\s*;\\s*"));
        for(String headerStr : headerList) {
            if(headerStr.contains("name=")) {
                int keyAssign = headerStr.indexOf("name=");
                this.header = headerStr.substring(keyAssign);
                System.out.println("Header " + this.header);
            }
        }
        this.content = content;
    }

    public String getHeader() {
        return this.header;
    }

    public String getContent() {
        return this.content;
    }

}
