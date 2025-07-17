package WebTest;

import java.io.IOException;
import java.net.URI;

public class PrioritizedHandlerTest extends OnlyGetHandlerTests {
    public PrioritizedHandlerTest() throws IOException {
        super(URI.create("http://localhost:8080/prioritized"));
    }
}
