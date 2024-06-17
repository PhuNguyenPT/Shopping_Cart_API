package com.example.shopping_cart.file;

import com.example.shopping_cart.handler.ExceptionResponse;
import com.example.shopping_cart.handler.FileExceptionHandler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final FileExceptionHandler customExceptionHandler;
    public FileService(FileRepository fileRepository, FileMapper fileMapper, FileExceptionHandler customExceptionHandler) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.customExceptionHandler = customExceptionHandler;
    }

    public ResponseEntity<?> saveFile(MultipartFile multipartFile) {
        try {
            var file = fileMapper.toFile(multipartFile);
            var savedFile = fileRepository.save(file);
            return ResponseEntity.ok("Save file " + savedFile.getName() + " successfully.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> saveFiles(@NotNull List<MultipartFile> multipartFiles) {

        List<File> files = multipartFiles.stream()
                .filter(multipartFile -> !multipartFile.isEmpty()) // Check that the file is not empty
                .map(fileMapper::toFile)
                .collect(Collectors.toUnmodifiableList());
        List<File> savedFiles = files.stream()
                .map(fileRepository::save)
                .collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok("Saved " + savedFiles.size() + " files successfully.");
    }


    public ResponseEntity<?> findFiles(String fileName) {
        try {
            List<FileResponseDTO> dtoList = fileRepository.findByNameContainingIgnoreCase(fileName)
                    .stream()
                    .map(fileMapper::toFileResponseDTO)
                    .collect(Collectors.toUnmodifiableList());
            if (dtoList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No files found with name: " + fileName);
            }

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File name must not be empty.");
        }
        try {
            Long fileDeleteCount = fileRepository.deleteByName(fileName);

            if (fileDeleteCount == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No files found with name: " + fileName);
            }

            return ResponseEntity.ok(String.format("Deleted %d file(s) %s successfully.", fileDeleteCount, fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
