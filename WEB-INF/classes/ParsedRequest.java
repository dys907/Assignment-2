import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ParsedRequest {
    private HttpRequest request;
    private String contentType;
    private int contentLength;
    private String type;
    private Hashtable<String, String> headers = new Hashtable<>();
    public List<Part> parts = new ArrayList<>();
    private String body;
    private String boundary;
    private boolean isBase64Encoded;
    private boolean isGet = false;
    private BufferedReader reader;

    ParsedRequest(HttpRequest request) throws IOException {
        this.request = request;
        //InputStream copyRequest = request.getInputStream();
        reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = "";
        int headerLine = 0;
        while (!(line = reader.readLine().trim()).isEmpty()) {
            System.out.println(line);
            System.out.println("parsecheck1");

            //Set POST or GET
            if (headerLine == 0) {
                if (line.contains("GET")) isGet = true;
                else isGet = false;
            }
            headerLine++;

            if (line.contains(":")) {
                while (line.trim().length() >= 0) {
                    int colon = line.indexOf(":");
                    if (colon == -1) break;
                    this.headers.put(line.substring(0, colon), line.substring(colon + 1));
                    line = reader.readLine();
                    System.out.println(line);
                    if (!(line = reader.readLine()).equals("")) break;
                }
            }

        }
        System.out.println("Exits first while block");
    }

    public void parse() throws IOException {
        System.out.println("=========content length================");
        System.out.println(headers.get("Content-Length"));

        int contentLength = Integer.parseInt(headers.get("Content-Length").trim());

        InputStream in = request.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] content = new byte[1];
        int bytesRead = -1;
        int counter = 0;



        while( counter < contentLength ) {
            System.out.println("enters whiel loop");
            bytesRead = in.read( content );
            baos.write(content);
            counter++;
            System.out.println(counter);
            System.out.println(bytesRead);
        }
        System.out.println(content.toString());


    }


//         System.out.println(headers.get("Content-Length"));

//        System.out.println("parsecheck3");
            //Multipart check
//            if (headers.containsKey("Content-Type")) {
//                this.contentType = headers.get("Content-Type");
//                //boundary delimiter
//                if (this.contentType.contains("multipart/form-data")) {
//                    int boundaryIndex = this.contentType.indexOf("=");
//                    this.boundary = this.contentType.substring(boundaryIndex + 1);
//                }
//            }
//            if (headers.containsKey("Content-Length")) {
//                this.contentLength = Integer.parseInt(headers.get("Content-Length"));
//            }
//            System.out.println(this.boundary);
//            System.out.println(this.contentLength);


//        String inputLine;
//        String requestString = "";
//        while (br.ready() && (inputLine = br.readLine()) != null) {
////            inputLine = br.readLine();
//            requestString += inputLine + "\n";
//        }
        //BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        //BufferedReader reader = new BufferedReader(new StringReader(requestString));

//        System.out.println("parsecheck4");

        //Body Parse
//        StringBuffer bodyBuffer = new StringBuffer();
//
//        line = reader.readLine();
//        int counter = 0;
//        String header = "";
//        String content = "";
//        isBase64Encoded = false;

//        System.out.println("parsecheck5");
//        while(line != null) {
//            content = "";
//            boolean imageData = false;
//            if (this.boundary != null) {
//                Part part;
////                System.out.println("===Boundary check====");
////                System.out.println(this.boundary);
//                if ((line.contains(this.boundary+"--"))) break;
//
//                if (line.contains(this.boundary)) {
//                    if (counter == 0) {
//                        counter++;
//                    } else {
//
////                        System.out.println("in else");
//                        line = reader.readLine();
//                        if (line.contains("Content-Disposition: form-data")) {
//                            header = line;
//                            line = reader.readLine();
////                            System.out.println("header: "+header);
//                        }
//                        if (line.contains("Content-Type:")) {
//                            if (line.contains("base64")) isBase64Encoded = true;
//                            imageData = true;
//                            line = reader.readLine();
//                        }
//
////                        System.out.println("before whiel loops");
//                        while (!line.contains(this.boundary)) {
//                            if (imageData) {
//                                content += line + "\n";
//                            } else {
//                                content += line;
//                            }
//                            line = reader.readLine();
//
////                            System.out.println("In while loop, content:");
////                            System.out.println(content);
//                        }
//
//                        if (content.length() > 500&& !isBase64Encoded)  {
//                            content = content.substring(1, content.length()-1);
//                        }
////                        System.out.println("Out of while loop, content:");
////                        System.out.println(content);
////
////                        System.out.println("current line:");
////                        System.out.println(line);
//
//                        part = new Part(header, content);
//                        parts.add(part);
//
//                    }
//                    //line = reader.readLine();
//                } else {
//                    bodyBuffer.append(line);
//                }
////                line = reader.readLine();
//            }
//            if (bodyBuffer.toString().isEmpty()) {
//                this.body = bodyBuffer.toString();
//            }
//        }





    public HttpRequest getRequest() {
        return this.request;
    }

    public String getContentType() {
        return this.contentType;
    }

    public boolean getBase64Encoded() {
        return this.isBase64Encoded;
    }

    public boolean getIsGet() {
        return this.isGet;
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
