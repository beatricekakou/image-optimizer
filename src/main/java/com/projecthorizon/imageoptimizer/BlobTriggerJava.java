package com.projecthorizon.imageoptimizer;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.io.ByteArrayInputStream;

public class BlobTriggerJava {
    // storage account's connection string
    private static final String STORAGE_CONNECTION_STRING = System.getenv("AzureWebJobsStorage");

    @FunctionName("ImageOptimizer")
    public void optimizeImage(
            @BlobTrigger(name = "inputImage", path = "image-input/{name}", dataType = "binary", connection = "AzureWebJobsStorage") byte[] inputBlob,
            @BindingName("name") String name,
            final ExecutionContext context
    ) {
        context.getLogger().info("image optimization function processed a blob. image name: " + name + "\nsize: " + inputBlob.length + " bytes");
        String fileNameWithoutExt = name.substring(0, name.lastIndexOf("."));
        try {
            byte[] optimizedImage = WebPConverter.convertToWebP(context,inputBlob,name, 80);
            uploadImage(optimizedImage, fileNameWithoutExt + ".webp", context);
            context.getLogger().info("image optimization function processed successfully. image name: " + name + "\n size input: " + inputBlob.length + " bytes size output: " + optimizedImage.length + " bytes");

        } catch (Exception e) {
            context.getLogger().severe("error during the image optimization: " + e.getMessage() +
                    "\nimage name: " + name + "\n size input: " + inputBlob.length);

        }
    }

    private void uploadImage(byte[] data, String imageName, ExecutionContext context) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(STORAGE_CONNECTION_STRING).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("image-output");

        BlobClient blobClient = containerClient.getBlobClient(imageName);
        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(data)) {
            blobClient.upload(dataStream, data.length, true);
            context.getLogger().info("blob uploaded successfully with name: " + imageName);
        } catch (Exception e) {
            context.getLogger().severe("failed to upload blob: " + e.getMessage());
        }
    }

}
