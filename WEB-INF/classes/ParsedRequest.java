import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ParsedRequest {
    private String request;
    private String contentType;
    private String type;
    private Hashtable<String, String> headers = new Hashtable<>();
    public List<Part> parts = new ArrayList<>();
    private String body;
    private String boundary;

    ParsedRequest(HttpRequest request) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line;

        //POST or GET check
        this.request = reader.readLine();
        this.type = this.request.split(" ")[0];

        //Header parse
        line = reader.readLine();
        while(line.length() > 0) {
            int colon = line.indexOf(":");
            this.headers.put(line.substring(0, colon), line.substring(colon + 1));
            line = reader.readLine();

        }

        //Multipart check
        if (headers.containsKey("Content-Type")) {
            this.contentType = headers.get("Content-Type");
            //boundary delimiter
            if (this.contentType.contains("multipart/form-data")) {
                int boundaryIndex = this.contentType.indexOf("boundary=");
                this.boundary = this.contentType.substring(boundaryIndex + 1);
            }
        }

        //Body Parse
        StringBuffer bodyBuffer = new StringBuffer();

        line = reader.readLine();
        while(line != null) {
            if (this.boundary != null) {
                String header = "";
                String content = "";
                Part part;
                while(!line.contains("--" + this.boundary)) {

                    if(line.contains("Content-Disposition: form-data")) {
                        header = line;
                    } else if (!line.trim().isEmpty()) {
                        content = line;
                    }
                    line = reader.readLine();
                }
                part = new Part(header,content);
                parts.add(part);
            } else {
                bodyBuffer.append(line);
            }
            line = reader.readLine();
        }
    }
}
