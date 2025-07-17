package WebTest;

import java.io.IOException;
import java.net.URI;

public class HistoryHandlerTest extends OnlyGetHandlerTests {
    public HistoryHandlerTest() throws IOException {
        super(URI.create("http://localhost:8080/history"));
    }
}
