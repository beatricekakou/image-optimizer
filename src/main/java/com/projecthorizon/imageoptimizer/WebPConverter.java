package com.projecthorizon.imageoptimizer;

import java.nio.file.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WebPConverter {

    /**
     * converts an image byte array to WebP format.
     *
     * @param inputBytes the byte array of the input image.
     * @param quality    the compression quality for WebP (from 0 to 100).
     * @return the byte array of the image converted to WebP format.
     * @throws IOException          if an error occurs during the command execution.
     * @throws InterruptedException if the process is interrupted.
     */
    public static byte[] convertToWebP(byte[] inputBytes, int quality) throws IOException, InterruptedException {
        File inputFile = File.createTempFile("input", ".tmp");
        File outputFile = File.createTempFile("output", ".webp");
        byte[] webpData = null;

        try {
            // write the byte array to the temporary input file
            FileOutputStream fos = new FileOutputStream(inputFile);
            fos.write(inputBytes);

            // build the command for cwebp
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "cwebp", "-q", String.valueOf(quality), inputFile.getAbsolutePath(), "-o", outputFile.getAbsolutePath());

            // start the process
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IOException("Error during conversion to WebP. Exit code: " + exitCode);
            }

            webpData = Files.readAllBytes(outputFile.toPath());
        } finally {
            // delete the temporary files
            inputFile.delete();
            outputFile.delete();
        }

        return webpData;
    }
}
