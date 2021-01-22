package be.intecbrussel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Class responsible for sorting a file
public class FileHandler {
    private final Path sortingDestination;

    public FileHandler(Path sortingDestination) {
        this.sortingDestination = sortingDestination;
    }

    // Recieves a file. Method resposible to call every step of the sorting.
    public void sortFile(Path filePath) {
        // If a path is directory, no need to handle it
        if (Files.isDirectory(filePath)){
            return;
        }

        // Extract the extension
        String extension = extractExtension(filePath);

        // If the file has an extension and the folder matching the extension doesn't exist, create it
        Path extensionFolderPath = null;
        try {
            extensionFolderPath = createExtensionFolderIfNotExist(filePath, extension);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Copy the file to the correct folder
        try {
            Files.copy(filePath, extensionFolderPath.resolve(filePath.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractExtension(Path filePath) {
        String extension = "";

        int index = filePath.toString().lastIndexOf('.');
        if (index > 0) {
            extension = filePath.toString().substring(index+1);
        }

        return extension;
    }

    private Path createExtensionFolderIfNotExist(Path filePath, String extension) throws IOException{
        Path extensionFolderPath = sortingDestination.resolve(Paths.get(extension));
            if (Files.isHidden(filePath)){
                if (extension.equalsIgnoreCase("")) {
                    extensionFolderPath = extensionFolderPath.resolve("hidden");
                } else {
                    extensionFolderPath = extensionFolderPath.getParent().resolve("hidden");
                }
            }
            else if (extensionFolderPath.equals(sortingDestination)) {
                extensionFolderPath = extensionFolderPath.resolve("no_extension");
            }

        if (!extensionFolderPath.toFile().exists()) {
                Files.createDirectory(extensionFolderPath);
        }

        return extensionFolderPath;
    }
}
