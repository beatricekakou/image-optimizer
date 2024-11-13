package com.projecthorizon.imageoptimizer;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class BlobTriggerJava {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     */
    @FunctionName("ImageOptimizer")
    public void optimizeImage(
            @BlobTrigger(name = "inputImage", path = "image-input/{name}", dataType = "binary", connection = "AzureWebJobsStorage") byte[] inputBlob,
            @BindingName("name") String name,
            @BlobOutput(name = "outputImage", path = "image-output/{nameWithoutExtension}.webp", dataType = "binary", connection = "AzureWebJobsStorage") OutputBinding<byte[]> outputBlob,
            final ExecutionContext context
    ) {
        context.getLogger().info("Image optimization function processed a blob. Image name: " + name + "\nSize: " + inputBlob.length + " Bytes");
        String nameWithoutExtension = name.substring(0, name.lastIndexOf("."));
        try {
            byte[] optimizedImage = WebPConverter.convertToWebP(inputBlob, 100);
            outputBlob.setValue(optimizedImage);
            context.getLogger().info("Image optimization function processed successfully. Image name: " + name + "\n Size input: " + inputBlob.length + " Bytes Size output: " + optimizedImage.length + " Bytes");

        } catch (Exception e) {
            context.getLogger().severe("Error during the image optimization: " + e.getMessage() +
                    "\nImage name: " + name + "\n Size input: " + inputBlob.length);

        }


    }

}
