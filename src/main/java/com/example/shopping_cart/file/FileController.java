package com.example.shopping_cart.file;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

//    public FileController(FileService fileService) {
//        this.fileService = fileService;
//    }

//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadFile(
//            @RequestParam("file") MultipartFile multipartFile) {
//        return fileService.saveFile(multipartFile);
//    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(
            @RequestParam("files")
            @NotEmpty(message = "Please provide files to upload")
            List<MultipartFile> multipartFiles) {
        return fileService.saveFiles(multipartFiles);
    }

    @GetMapping("/search")
    public ResponseEntity<?> findFileList(
            @RequestParam(value = "file-name", required = false)
            String fileName) {
        return fileService.findFiles(fileName);
    }

    @DeleteMapping("/delete/{file-name}")
    public ResponseEntity<?> deleteFile(
            @PathVariable("file-name")
            @NotNull(message = "File name must be not null")
            @NotBlank(message = "File name must be not blank")
            String fileName) {
        return fileService.deleteFile(fileName);
    }
}
