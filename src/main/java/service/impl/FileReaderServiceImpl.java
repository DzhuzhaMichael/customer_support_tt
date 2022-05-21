package service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import service.FileReaderService;

public class FileReaderServiceImpl implements FileReaderService {
    @Override
    public List<String> read(String sourceFilePath) {
        try {
            return Files.readAllLines(Paths.get(sourceFilePath));
        } catch (IOException e) {
            throw new RuntimeException("Can`t read file by the path "
            + sourceFilePath, e);
        }
    }
}
