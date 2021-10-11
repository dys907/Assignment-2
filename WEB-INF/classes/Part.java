import java.util.List;
import java.util.Arrays;
import java.util.Hashtable;

public class Part {
    private String header;
    private String content;
    private byte[] byteContent;

    public Part(String headers, String content) {
        List<String> headerList = Arrays.asList(headers.split("\\s*;\\s*"));
        for(String headerStr : headerList) {
            if(headerStr.contains("name=")) {
                int keyAssign = headerStr.indexOf("=");
                this.header = headerStr.substring(keyAssign + 1);
                System.out.println("Header " + this.header);
            }
        }
        this.content = content;
    }
    //for images
    public Part(String headers, byte[] byteContent) {
        List<String> headerList = Arrays.asList(headers.split("\\s*;\\s*"));
        for(String headerStr : headerList) {
            if(headerStr.contains("name=")) {
                int keyAssign = headerStr.indexOf("=");
                this.header = headerStr.substring(keyAssign + 1);
                System.out.println("Header " + this.header);
            }
        }
        this.byteContent = byteContent;
    }

    public String getHeader() {
        return this.header;
    }

    public String getContent() {
        return this.content;
    }

    public byte[] getByteContent() {return this.byteContent;}

}
