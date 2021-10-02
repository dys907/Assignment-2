public interface Servlet {
    public void doGet(Request request, Response response);

    public void doPost(Request request, Response response);
}