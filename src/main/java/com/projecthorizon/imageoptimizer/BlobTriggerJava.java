package com.projecthorizon.imageoptimizer;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class BlobTriggerJava {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     */
    @FunctionName("ImageOptimizer")
    public void optimizeImage(
        @BlobTrigger(name = "inputImage", path = "image-input/{name}", dataType = "binary",connection ="AzureWebJobsStorage" ) byte[] inputBlob,
        @BindingName("name") String name,
        @BlobOutput(name = "outputImage", path = "image-output/{name}", dataType = "binary",connection ="AzureWebJobsStorage" ) OutputBinding<byte[]> outputBlob,
        final ExecutionContext context
    ) {
        context.getLogger().info("Image optimization function processed a blob. Name: " + name + "\nSize: " + inputBlob.length + " Bytes");
        outputBlob.setValue(inputBlob);
    }

}
