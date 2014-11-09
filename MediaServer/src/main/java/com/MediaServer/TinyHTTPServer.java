package com.MediaServer;
/**
 * Created by alexbiju on 11/8/14.
 */
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.io.PrintStream;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.net.URL;
        import java.util.LinkedList;
        import java.util.List;
        import java.util.Scanner;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

public class TinyHTTPServer {

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(8888);

        while (true) {
            final Socket connection = server.accept();
            new Thread(new Runnable(){
                public void run() {
                    RequestHandler handler = new RequestHandler();
                    handler.handle(connection);
                }
            }).start();
        }
    }

    public static class RequestHandler {

        public void handle(Socket socket) {
            try {
                Scanner scanner = new Scanner(socket.getInputStream(), "US-ASCII");
                String path = getPath(scanner.nextLine());

                Response response = find(path);

                PrintStream out = new PrintStream(socket.getOutputStream());

                for (String header : response.headers) {
                    out.print(header);
                    out.print("\r\n");
                }

                out.print("\r\n");
                if (response.url != null)
                    writeEntity(response.url.openStream(), socket.getOutputStream());
                out.print("\r\n");

                out.flush();
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getPath(String requestLine) throws IOException {
            Matcher matcher = Pattern.compile("GET (/\\S*) HTTP/1\\.1").matcher(requestLine);
            matcher.find();
            return matcher.group(1);
        }

        private Response find(String path) {

            if (path.equals("/"))
                path = "/index.html";

            Response response = new Response();
            URL url = RequestHandler.class.getResource(path);

            if (url == null) {
                response.headers.add("HTTP/1.1 404 NOT FOUND");
            } else {
                response.url = url;
                response.headers.add("HTTP/1.1 200 OK");

                String type = "application/octet-stream";
                String extension = url.toString();

                if (extension.endsWith(".mp3"))
                    type = "audio/mp3";
                else if (extension.endsWith(".html"))
                    type = "text/html; charset=utf-8";

                response.headers.add("Content-Type: " + type);
            }

            return response;
        }

        private void writeEntity(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[1024];
            int read = -1;

            while ((read = in.read(buffer, 0, buffer.length)) > -1) {
                out.write(buffer, 0, read);
            }
        }

    }

    public static class Response {

        public List<String> headers = new LinkedList<String>();
        public URL url;

    }
}
