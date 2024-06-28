package com.example.shopping_cart.file;

import com.example.shopping_cart.product.Product;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
//    private final FileExceptionHandler customExceptionHandler;
//    public FileService(FileRepository fileRepository, FileMapper fileMapper, FileExceptionHandler customExceptionHandler) {
//        this.fileRepository = fileRepository;
//        this.fileMapper = fileMapper;
//        this.customExceptionHandler = customExceptionHandler;
//    }

//    public ResponseEntity<?> saveFile(MultipartFile multipartFile) {
//        try {
//            var file = FileMapper.toFile(multipartFile);
//            var savedFile = fileRepository.save(file);
//            return ResponseEntity.ok("Save file " + savedFile.getName() + " successfully.");
//        } catch (RuntimeException e) {
//            throw new RuntimeException("Cannot save file " + multipartFile.getOriginalFilename());
//        }
//    }

    @Transactional
    public ResponseEntity<?> saveFiles(
            @NotNull List<MultipartFile> multipartFiles
    ) {

        List<File> files = multipartFiles.stream()
                .filter(multipartFile -> !multipartFile.isEmpty()) // Check that the file is not empty
                .map(FileMapper::toFile)
                .toList();
        List<File> savedFiles = files.stream()
                .map(fileRepository::save)
                .toList();

//        return ResponseEntity.ok("Saved " + savedFiles.size() + " files successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                "Saved " + savedFiles.size() + " files successfully."
        );
    }

    @Transactional
    public List<FileResponseDTO> saveFilesByProduct(
            Product product,
            @NotNull List<MultipartFile> multipartFiles) {

        List<File> files = multipartFiles.stream()
                .filter(multipartFile -> !multipartFile.isEmpty())
                .map(multipartFile -> FileMapper.toFileSave(multipartFile, product))
                .toList();
        if (files.isEmpty()) {
            throw new MultipartException("File(s) upload not found");
        }
        List<File> savedFiles = files.stream()
                .map(fileRepository::save)
                .toList();
        List<FileResponseDTO> fileResponseDTOList = savedFiles.stream()
                .map(FileMapper::toFileResponseDTOSave)
                .toList();
        fileResponseDTOList
                .forEach(fileResponseDTO ->
                        fileResponseDTO.setMessage("Save " +
                                        fileResponseDTO.getName() +
                                        " successfully"));
//        return ResponseEntity.ok("Saved " + savedFiles.size() + " files successfully.");
        return fileResponseDTOList;
    }

    public ResponseEntity<?> findFiles(String fileName) {
        try {
            List<FileResponseDTO> dtoList = fileRepository.findByNameContainingIgnoreCase(fileName)
                    .stream()
                    .map(FileMapper::toFileResponseDTO)
                    .toList();
            if (dtoList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No files found with name: " + fileName);
            }

            return ResponseEntity.ok(dtoList);
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot find File " + fileName);
        }
    }

    @Transactional
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
            throw new RuntimeException("Cannot delete File " + fileName);
        }
    }


    @Transactional
    public FileResponseDTO updateFile(
            MultipartFile multipartFile,
            Product product,
            Long fileId) {
        File file = fileRepository.findById(fileId);
        if (file == null || !file.getProduct().equals(product)) {
            throw new EntityNotFoundException("File not found for the given product");
        }
        file.setName(multipartFile.getOriginalFilename());
        file.setFileType(multipartFile.getContentType());
        file.setSize(BigInteger.valueOf(multipartFile.getSize()));
        try {
            file.setFileContent(
                    FileMapper.toCompressedFileByteBase64(
                            multipartFile.getBytes()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException("Cannot compress file byte in Base64");
        }
        File savedFile = fileRepository.save(file);
        return FileMapper.toFileResponseDTOUpdate(savedFile);
    }

    public List<File> saveAllFilesByProduct(
            @NotNull List<MultipartFile> multipartFiles,
            Product product
    ) {
        List<File> files = multipartFiles.stream()
                .filter(multipartFile -> !multipartFile.isEmpty())
                .map(multipartFile -> FileMapper.toFileSave(multipartFile, product))
                .collect(Collectors.toList());
        return fileRepository.saveAll(files);
    }
}
