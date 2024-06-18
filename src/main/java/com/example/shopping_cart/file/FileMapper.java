package com.example.shopping_cart.file;

import com.example.shopping_cart.product.Product;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;

@Service
public class FileMapper {

    public static File toFile(
            @NotNull MultipartFile multipartFile
    ) {
        try {
            return File.builder()
                    .name(multipartFile.getOriginalFilename())
                    .fileType(multipartFile.getContentType())
                    .size(BigInteger.valueOf(multipartFile.getSize()))
                    .fileContent(toCompressedFileByteBase64(multipartFile.getBytes()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Error converting MultipartFile to File");
        }
    }

    public static byte[] toCompressedFileByteBase64(
            byte[] fileByte
    ) {
        try {
            var compressedFileByte = FileUtil.compressByte(fileByte);
            return Base64.encodeBase64(compressedFileByte, true);
        } catch (Exception e) {
            throw new RuntimeException("Error compressing file byte array", e);
        }
    }

    public static FileResponseDTO toFileResponseDTO(
            @NotNull File file
    ) {
        var compressedFileByte = Base64.decodeBase64(file.getFileContent(), 0, file.getFileContent().length);
        var fileByte = FileUtil.decompressByte(compressedFileByte);
//        var fileByte = FileUtil.decompressFile(file.getFileContent());
//        System.out.println(fileByte);
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .fileType(file.getFileType())
                .size(file.getSize())
                .fileByte(fileByte)
                .build();
    }

    public static File toFileSave(
            @NotNull MultipartFile multipartFile,
            Product product
    ){
        try {
            return File.builder()
                    .name(multipartFile.getOriginalFilename())
                    .fileType(multipartFile.getContentType())
                    .size(BigInteger.valueOf(multipartFile.getSize()))
                    .fileContent(toCompressedFileByteBase64(multipartFile.getBytes()))
                    .product(product)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Error converting MultipartFile to File");
        }
    }

    public static FileResponseDTO toFileResponseDTOSave(
            @NotNull File file
    ) {
//        var compressedFileByte = Base64.decodeBase64(file.getFileContent(), 0, file.getFileContent().length);
//        var fileByte = FileUtil.decompressByte(compressedFileByte);
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .fileType(file.getFileType())
                .size(file.getSize())
                .build();
    }

    public FileResponseDTO toFileResponseDTOUpdate(
            @NotNull File file
    ) {
//        var compressedFileByte = Base64.decodeBase64(file.getFileContent(), 0, file.getFileContent().length);
//        var fileByte = FileUtil.decompressByte(compressedFileByte);
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .fileType(file.getFileType())
                .size(file.getSize())
                .build();
    }

    public static FileResponseDTO toFileResponseDTOSearch(
            @NotNull File file
    ) {
        var compressedFileByte = Base64.decodeBase64(file.getFileContent(), 0, file.getFileContent().length);
        var fileByte = FileUtil.decompressByte(compressedFileByte);
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .fileType(file.getFileType())
                .size(file.getSize())
                .fileByte(fileByte)
                .build();
    }
}
