import com.projecthorizon.imageoptimizer.WebPConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class WebPConverterTest {

    private static byte[] sampleImageBytes;

    @BeforeAll
    static void setup() throws IOException {
        String inputImagePath = "src/main/test/resources/test-image.jpeg";
        sampleImageBytes = Files.readAllBytes(Paths.get(inputImagePath));
    }

    @AfterAll
    static void cleanup() {
        sampleImageBytes = null;
    }

    @Test
    void testConvertToWebP() throws IOException, InterruptedException {
        int quality = 80;
        byte[] webpBytes = WebPConverter.convertToWebP(sampleImageBytes, quality);

        assertNotNull(webpBytes, "the webp byte array should not be null");
        assertTrue(webpBytes.length > 0, "the webp byte array should not be empty");
    }

    @Test
    void testConvertToWebPLowQuality() throws IOException, InterruptedException {
        int quality = 0;
        byte[] webpBytes = WebPConverter.convertToWebP(sampleImageBytes, quality);

        assertNotNull(webpBytes, "the webpbyte array should not be null for low quality");
        assertTrue(webpBytes.length > 0, "the webpbyte array should not be empty for low quality");
    }

    @Test
    void testConvertToWebPHighQuality() throws IOException, InterruptedException {
        int quality = 100;
        byte[] webpBytes = WebPConverter.convertToWebP(sampleImageBytes, quality);

        assertNotNull(webpBytes, "the webp byte array should not be null for high quality");
        assertTrue(webpBytes.length > 0, "the webp byte array should not be empty for high quality");
    }


    @Test
    void testConvertNullInput() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            WebPConverter.convertToWebP(null, 80);
        });

        assertEquals(NullPointerException.class, exception.getClass(), "expected NullPointerException");
    }
}

