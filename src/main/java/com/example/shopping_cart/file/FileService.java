package com.example.shopping_cart.file;

import com.example.shopping_cart.exception.CustomExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final CustomExceptionHandler customExceptionHandler;
    public FileService(FileRepository fileRepository, FileMapper fileMapper, CustomExceptionHandler customExceptionHandler) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
        this.customExceptionHandler = customExceptionHandler;
    }

    public ResponseEntity<?> saveFile(MultipartFile multipartFile) {
        try {
            var file = fileMapper.toFile(multipartFile);
            var savedFile = fileRepository.save(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    String.format("Save file %s successfully.", savedFile.getName()));
        } catch (RuntimeException e) {
            throw customExceptionHandler.handleUploadFileException(e, multipartFile.getOriginalFilename());
        }
    }

    public ResponseEntity<?> findFile(String fileName) {
        Optional<FileResponseDTO> dto = fileRepository.findByNameContainingIgnoreCase(fileName)
                .map(fileMapper::toFileResponseDTO);
        if (dto.isEmpty()) {
            throw customExceptionHandler.handleFindByNameException(fileName);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    public ResponseEntity<?> deleteFile(String fileName) {
        Long fileDeleteCount = fileRepository.deleteByName(fileName);
        if (fileDeleteCount <= 0) {
            throw customExceptionHandler.handleDeleteDataException(fileName);
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                String.format("Delete %d file(s) %s successfully.", fileDeleteCount, fileName)
        );
    }
}
