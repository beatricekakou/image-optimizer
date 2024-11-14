## Image Optimizer Azure Function

### Description

This project implements an **Azure Function** that optimizes images by uploading them to an **Azure Blob Storage**, converting them to **WebP** format using the **Google WebP CLI**, and saving them in another Blob Storage container. The function is triggered whenever a new blob is added to the `image-input` container.

To integrate the Google WebP CLI into the project, it was containerized along with the application code, ensuring that the CLI tools are available within the execution environment.

### Project Structure

- **`BlobTriggerJava`**: The main class containing the Azure Function `optimizeImage`, which is triggered by a blob trigger.
- **`WebPConverter`**: The class responsible for converting images to WebP format using the Google WebP CLI.

### Prerequisites

- **Java Development Kit (JDK)**: Ensure you have JDK 8 or later installed.
- **Azure Functions Core Tools**: For testing and publishing the Azure Function.
- **Azure Storage Account**: Required for Blob Storage.
- **Docker**: Needed to build and run the containerized application.
- **WebP Tools**: Install `cwebp` and `gif2webp` on your PC and ensure they are accessible in your system's PATH.

### Configuration

#### Clone the Repository

Download or clone this project to your local computer.

```bash
git clone https://github.com/yourusername/image-optimizer-azure-function.git
cd image-optimizer-azure-function
```

#### Set Environment Variables

Set `AzureWebJobsStorage` with the connection string of your Azure Storage Account.

#### Create Blob Storage Containers

- **`image-input`**: Container where original images will be uploaded.
- **`image-output`**: Container where optimized images will be saved.

### How It Works

#### Image Upload

Upload an image to the `image-input` container. It can be in any supported format (JPEG, PNG, GIF, etc.).

#### Function Trigger

The Azure Function `optimizeImage` is automatically triggered thanks to the **BlobTrigger**.

#### Image Conversion

- The function reads the incoming blob.
- It calls the `convertToWebP` method of the `WebPConverter` class to convert the image to WebP format with 80% quality.
- **Conversion uses the Google WebP CLI tools (`cwebp` and `gif2webp`) via system commands.**
- To ensure these tools are available during execution, the application is **containerized along with the Google WebP CLI tools**.

#### Saving the Optimized Image

The converted image is saved in the `image-output` container with a `.webp` extension.

#### Logging

The function logs information about the process, including any errors.

### Running Locally

To run and test the function locally:

#### Install Azure Functions Core Tools

If you haven't already, install the [Azure Functions Core Tools](https://docs.microsoft.com/azure/azure-functions/functions-run-local).

#### Install WebP Tools on Your PC

Make sure `cwebp` and `gif2webp` are installed on your PC and accessible in your system's PATH.

- **Windows**: Download the binaries from [here](https://developers.google.com/speed/webp/download) and add them to your system PATH.
- **Linux**: Install via the package manager, e.g., `sudo apt-get install webp`.
- **macOS**: Install using Homebrew with `brew install webp`.

#### Build the Docker Image

Since the application is containerized with the Google WebP CLI, you need to build the Docker image:

```bash
docker build -t image-optimizer-azure-function:latest .
```

#### Run the Docker Container

Run the container locally:

```bash
docker run -p 8080:80 -e AzureWebJobsStorage="<your_connection_string>" image-optimizer-azure-function:latest
```

#### Start the Storage Emulator

- If you're using **Azurite** (cross-platform), make sure it's running.
- Configure `AzureWebJobsStorage` in the `local.settings.json` file with the emulator's connection string if testing without Docker.

#### Test the Function

- Upload an image to the `image-input` container using tools provided by the Azure Functions Core Tools.
- Verify that the optimized image appears in the `image-output` container.

### Deployment to Azure

To deploy the function to Azure:

#### Create an Azure Function App

Using the Azure Portal or CLI, create a new Function App that supports Docker containers.

#### Push Docker Image to Azure Container Registry (ACR)

1. **Build** the Docker image:

   ```bash
   docker build -t youracrname.azurecr.io/image-optimizer-azure-function:latest .
   ```

2. **Push** the image to ACR:

   ```bash
   docker push youracrname.azurecr.io/image-optimizer-azure-function:latest
   ```

#### Configure the Function App

- Set the image source to your ACR and select the `image-optimizer-azure-function` image.
- Configure application settings with environment variables such as `AzureWebJobsStorage`.

#### Verify Functionality

- Upload an image to the `image-input` container of your Storage Account.
- Check the `image-output` container for the optimized image.

### Dependencies

- **Azure Functions Java Library**: For developing Azure Functions in Java.
- **Azure Storage Blob SDK**: For interacting with Blob Storage.
- **Java Image I/O**: For image input/output operations.
- **Google WebP CLI Tools (`cwebp`, `gif2webp`)**: For image conversion to WebP format.
- **Docker**: For containerizing the application along with the CLI tools.

### Important Notes

#### Security

Ensure you do not expose connection strings or other sensitive credentials in the source code or public repositories.

#### System Limitations

The function executes system commands to convert images. By containerizing the application with the Google WebP CLI tools, we ensure that the necessary tools are available in the execution environment, both locally and on Azure. However, for local development and testing, you need to have `cwebp` and `gif2webp` installed on your PC.

#### Error Handling

The function includes basic error handling. It might be necessary to implement a more robust logging or notification system for production applications.

---

I hope this README helps you understand the project. If you need further information, don't hesitate to ask!
