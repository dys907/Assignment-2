import java.net.http.HttpRequest;

public interface Servlet {
    public void doGet(HttpRequest request, HttpResponse response);

    public void doPost(HttpRequest request, HttpResponse response);
}