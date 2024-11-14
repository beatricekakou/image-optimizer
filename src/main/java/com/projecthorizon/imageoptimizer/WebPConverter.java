package com.projecthorizon.imageoptimizer;

import com.microsoft.azure.functions.ExecutionContext;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Objects;

public class WebPConverter {

    private static final String DOT = ".";
    private static final String INPUT_FILE_PREFIX = "input";
    private static final String OUTPUT_FILE_PREFIX = "output";
    private static final String WEBP_EXTENSION = ".webp";
    private static final String GIF_FORMAT = "gif";
    private static final String GIF2WEBP_COMMAND = "gif2webp";
    private static final String CWEBP_COMMAND = "cwebp";
    private static final String QUALITY_FLAG = "-q";
    private static final String OUTPUT_FLAG = "-o";
    private static final String CONVERSION_ERROR_MESSAGE = "Error during conversion to WebP. Exit code: ";

    /**
     * Converts an image byte array to WebP format.
     *
     * @param inputBytes the byte array of the input image.
     * @param imageName  the name of the input image file.
     * @param quality    the compression quality for WebP (from 0 to 100).
     * @return the byte array of the image converted to WebP format.
     * @throws IOException          if an error occurs during the command execution.
     * @throws InterruptedException if the process is interrupted.
     */
    public static byte[] convertToWebP(ExecutionContext context, byte[] inputBytes, String imageName, int quality)
            throws IOException, InterruptedException {
        if (Objects.isNull(inputBytes)) {
            context.getLogger().severe("Input bytes cannot be null.");
            throw new IOException("Input bytes cannot be null.");
        }
        // determine the image format based on the file extension
        String formatName = imageName.substring(imageName.lastIndexOf(DOT) + 1);
        File inputFile = File.createTempFile(INPUT_FILE_PREFIX, DOT + formatName);
        File outputFile = File.createTempFile(OUTPUT_FILE_PREFIX, WEBP_EXTENSION);
        byte[] webpData = null;

        try {
            // write the image bytes to the temporary input file
            try (FileOutputStream fos = new FileOutputStream(inputFile)) {
                fos.write(inputBytes);
            }

            // build the command
            ProcessBuilder processBuilder;
            if (GIF_FORMAT.equalsIgnoreCase(formatName) ) {
                processBuilder = new ProcessBuilder(
                        GIF2WEBP_COMMAND, QUALITY_FLAG, String.valueOf(quality),
                        inputFile.getAbsolutePath(), OUTPUT_FLAG, outputFile.getAbsolutePath());
            } else {
                processBuilder = new ProcessBuilder(
                        CWEBP_COMMAND, QUALITY_FLAG, String.valueOf(quality),
                        inputFile.getAbsolutePath(), OUTPUT_FLAG, outputFile.getAbsolutePath());
            }

            // start the process
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                context.getLogger().severe(CONVERSION_ERROR_MESSAGE + exitCode);
                throw new IOException(CONVERSION_ERROR_MESSAGE + exitCode);
            }

            // read the converted WebP data from the output file
            webpData = Files.readAllBytes(outputFile.toPath());
        } finally {
            // delete the temporary files
            inputFile.delete();
            outputFile.delete();
        }

        return webpData;
    }
}
