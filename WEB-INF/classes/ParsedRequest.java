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
    private boolean isBase64Encoded;

    ParsedRequest(String request) throws IOException {
        //BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        BufferedReader reader = new BufferedReader(new StringReader(request));
        String line;

//        System.out.println("parsecheck1");
        //POST or GET check
        this.request = reader.readLine();
        this.type = this.request.split(" ")[1];

//        System.out.println("parsecheck2");
        //Header parse
        line = reader.readLine();
//        System.out.println(line);
        while(line.length() > 0) {
            int colon = line.indexOf(":");
            this.headers.put(line.substring(0, colon), line.substring(colon + 1));
            line = reader.readLine();

        }
//        System.out.println("parsecheck3");
        //Multipart check
        if (headers.containsKey("Content-Type")) {
            this.contentType = headers.get("Content-Type");
            //boundary delimiter
            if (this.contentType.contains("multipart/form-data")) {
                int boundaryIndex = this.contentType.indexOf("=");
                this.boundary = this.contentType.substring(boundaryIndex + 1);
            }
        }
//        System.out.println("parsecheck4");

        //Body Parse
        StringBuffer bodyBuffer = new StringBuffer();

        line = reader.readLine();
        int counter = 0;
        String header = "";
        String content = "";
        isBase64Encoded = false;

//        System.out.println("parsecheck5");
        while(line != null) {
            content = "";
            boolean imageData = false;
            if (this.boundary != null) {
                Part part;
//                System.out.println("===Boundary check====");
//                System.out.println(this.boundary);
                if ((line.contains(this.boundary+"--"))) break;

                if (line.contains(this.boundary)) {
                    if (counter == 0) {
                        counter++;
                    } else {

//                        System.out.println("in else");
                        line = reader.readLine();
                        if (line.contains("Content-Disposition: form-data")) {
                            header = line;
                            line = reader.readLine();
//                            System.out.println("header: "+header);
                        }
                        if (line.contains("Content-Type:")) {
                            if (line.contains("base64")) isBase64Encoded = true;
                            imageData = true;
                            line = reader.readLine();
                        }

//                        System.out.println("before whiel loops");
                        while (!line.contains(this.boundary)) {
                            if (imageData) {
                                content += line + "\n";
                            } else {
                                content += line;
                            }
                            line = reader.readLine();

//                            System.out.println("In while loop, content:");
//                            System.out.println(content);
                        }

                        if (content.length() > 500&& !isBase64Encoded)  {
                            content = content.substring(1, content.length()-1);
                        }
//                        System.out.println("Out of while loop, content:");
//                        System.out.println(content);
//
//                        System.out.println("current line:");
//                        System.out.println(line);

                        part = new Part(header, content);
                        parts.add(part);

                    }
                    //line = reader.readLine();
                } else {
                    bodyBuffer.append(line);
                }
//                line = reader.readLine();
            }
            if (bodyBuffer.toString().isEmpty()) {
                this.body = bodyBuffer.toString();
            }
        }
    }


    public String getRequest() {
        return this.request;
    }

    public String getContentType() {
        return this.contentType;
    }

    public boolean getBase64Encoded() {
        return this.isBase64Encoded;
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
