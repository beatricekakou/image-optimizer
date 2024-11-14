import com.projecthorizon.imageoptimizer.WebPConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.microsoft.azure.functions.ExecutionContext;
import java.util.logging.Logger;

public class WebPConverterTest {

    private static byte[] imageBytes;
    private static byte[] gifBytes;

    @BeforeAll
    static void setup() throws IOException {
        String inputImagePath = "src/main/test/resources/test-image.jpeg";
        String inputGifPath = "src/main/test/resources/homer.gif";

        imageBytes = Files.readAllBytes(Paths.get(inputImagePath));
        gifBytes = Files.readAllBytes(Paths.get(inputGifPath));
    }

    @Test
    public void testConvertJpegToWebP() throws IOException, InterruptedException {
        ExecutionContext context = new TestExecutionContext();

        byte[] webpBytes = WebPConverter.convertToWebP(context, imageBytes, "test-image.jpeg", 75);

        assertNotNull(webpBytes, "the converted WebP byte array should not be null.");
        assertTrue(webpBytes.length > 0, "the converted WebP byte array should not be empty.");
    }

    @Test
    public void testConvertGifToWebP() throws IOException, InterruptedException {
        ExecutionContext context = new TestExecutionContext();

        byte[] webpBytes = WebPConverter.convertToWebP(context, gifBytes, "homer.gif", 75);

        assertNotNull(webpBytes, "the converted WebP byte array should not be null.");
        assertTrue(webpBytes.length > 0, "the converted WebP byte array should not be empty.");
    }

    @Test
    public void testConvertNullInput() {
        ExecutionContext context = new TestExecutionContext();

        assertThrows(IOException.class, () -> {
            WebPConverter.convertToWebP(context, null, "test-image.jpeg", 75);
        }, "converting null input bytes should throw an IOException.");
    }

    @Test
    public void testInvalidQualityValue() {
        ExecutionContext context = new TestExecutionContext();

        assertThrows(IOException.class, () -> {
            WebPConverter.convertToWebP(context, imageBytes, "test-image.jpeg", -10);
        }, "using an invalid quality value should throw an IOException.");
    }

    private static class TestExecutionContext implements ExecutionContext {
        private final Logger logger = Logger.getLogger("TestExecutionContext");

        @Override
        public Logger getLogger() {
            return logger;
        }

        @Override
        public String getInvocationId() {
            return "test-invocation-id";
        }

        @Override
        public String getFunctionName() {
            return "test-function";
        }
    }
}


