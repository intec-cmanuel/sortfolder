package be.intecbrussel.app;

import be.intecbrussel.config.FolderPaths;
import be.intecbrussel.service.FileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SortingApp {
    public static void main(String[] args) {
        Path unsorted = FolderPaths.UNSORTED_PATH;
        Path sorted = FolderPaths.SORTED_PATH;

        // Stop the app if the unsorted folder does not exist
        if (!unsorted.toFile().exists()) {
            return;
        }

        // If the destination folder for the sorted files does not exist, create it, and all folders leading to it.
        try {
            createSortedFolderIfNotExist(sorted);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Go through all the files of the unsorted folder
        try {
            goThroughFiles(unsorted, sorted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createSortedFolderIfNotExist(Path sorted) throws IOException{
        if (!sorted.toFile().exists()) {
                Files.createDirectories(sorted);
        }
    }

    private static void goThroughFiles(Path unsorted, Path sorted) throws IOException {
        FileHandler fileHandler = new FileHandler(sorted);

        // Remove the unsorted folder itself from the files to sort
        Files.walk(unsorted)
                .filter(path -> !path.equals(unsorted))
                .forEach(fileHandler::sortFile);
    }
}
