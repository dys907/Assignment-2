import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
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

    ParsedRequest(String request) throws IOException {
        //BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        BufferedReader reader = new BufferedReader(new StringReader(request));
        String line;

        //POST or GET check
        this.request = reader.readLine();
        this.type = this.request.split(" ")[1];
        System.out.println("debug0");

        //Header parse
        line = reader.readLine();
        while(line.length() > 0) {
            int colon = line.indexOf(":");
            this.headers.put(line.substring(0, colon), line.substring(colon + 1));
            line = reader.readLine();

        }
        System.out.println("debug1");

        //Multipart check
        if (headers.containsKey("Content-Type")) {
            this.contentType = headers.get("Content-Type");
            //boundary delimiter
            if (this.contentType.contains("multipart/form-data")) {
                int boundaryIndex = this.contentType.indexOf("boundary=");
                this.boundary = this.contentType.substring(boundaryIndex);
            }
        }

        System.out.println("debug2");
        System.out.println("boundary");
        System.out.println(this.boundary);
        System.out.println("request");
        System.out.println(this.request);
        System.out.println("type");
        System.out.println(this.type);

        //Body Parse
        StringBuffer bodyBuffer = new StringBuffer();


        line = reader.readLine();
        int counter = 0;
        while(line != null) {
            System.out.println("debug3");
            if (this.boundary != null) {
                System.out.println("debug4");
                String header = "";
                String content = "";
                Part part;

                if (line.contains("Content-Disposition: form-data")) {
                    header = line;
                    System.out.println("debug5");
                    System.out.println(header);
                } else if (line.contains("--" + this.boundary)) {
                    if(counter == 0) {
                        counter++;
                    } else {
                        part = new Part(header,content);
                        parts.add(part);
                        System.out.println("debug 5.5");
                    }
                }else if (!line.trim().isEmpty()) {
                    System.out.println("debug6");
                    content = line;
                    System.out.println(content);
                }
                //line = reader.readLine();
                System.out.println("debug7");
            } else {
                bodyBuffer.append(line);
            }
            System.out.println("debug8");
            line = reader.readLine();
            System.out.println("debug9");
        }
        if(bodyBuffer.toString().isEmpty()) {
            this.body = bodyBuffer.toString();
        }

    }


    public String getRequest() {
        return this.request;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getType() {
        return this.type;
    }

    public Hashtable<String, String> getHeaders() {
        return this.headers;
    }

    public List<Part> getParts() {
        return this.parts;
    }

    public String getBody() {
        return this.body;
    }

    public String getBoundary() {
        return this.boundary;
    }
}
