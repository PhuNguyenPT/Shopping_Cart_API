package com.example.shopping_cart.file;

import com.example.shopping_cart.product.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.List;

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
            return Base64.getEncoder().encode(compressedFileByte);
        } catch (Exception e) {
            throw new RuntimeException("Error compressing file byte array", e);
        }
    }

    public static FileResponseDTO toFileResponseDTO(
            @NotNull File file
    ) {
        var compressedFileByte = Base64.getDecoder().decode(file.getFileContent());
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
                .fileByte(file.getFileContent())
                .build();
    }

    public static FileResponseDTO toFileResponseDTOSearch(
            @NotNull File file
    ) {
        var compressedFileByte = Base64.getDecoder().decode(file.getFileContent());
        var fileByte = FileUtil.decompressByte(compressedFileByte);
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .fileType(file.getFileType())
                .size(file.getSize())
                .fileByte(fileByte)
                .build();
    }

    public static FileResponseDTO toFileResponseDTOUpdate(
            @NotNull File file
    ) {
        var compressedFileByte = Base64.getDecoder().decode(file.getFileContent());
        var fileByte = FileUtil.decompressByte(compressedFileByte);
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .fileType(file.getFileType())
                .size(file.getSize())
                .fileByte(fileByte)
                .build();
    }

    public static FileResponseDTO toFileResponseDTOUpdateProduct(
            @NotNull File file,
            @NotNull FileResponseDTO fileResponseDTO
    ) {
        if (file.getId() == fileResponseDTO.getId()) {
            return fileResponseDTO;
        }
        var compressedFileByte = Base64.getDecoder().decode(file.getFileContent());
        var fileByte = FileUtil.decompressByte(compressedFileByte);
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .fileType(file.getFileType())
                .size(file.getSize())
                .fileByte(fileByte)
                .build();
    }

    public static FileResponseDTO toFileResponseDTOSaveProductFiles(
            @NotNull File file,
            @NotNull List<FileResponseDTO> fileResponseDTOList
    ) {
        for (FileResponseDTO fileResponseDTO : fileResponseDTOList) {
            if (file.getId() == fileResponseDTO.getId()) {
                return fileResponseDTO;
            }
        }
        var compressedFileByte = Base64.getDecoder().decode(file.getFileContent());
        var fileByte = FileUtil.decompressByte(compressedFileByte);
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .fileType(file.getFileType())
                .size(file.getSize())
                .fileByte(fileByte)
                .build();
    }
    public static FileResponseDTO toFileResponseDTOShoppingCart(
            @NotNull File file
    ) {
        var compressedFileByte = Base64.getDecoder().decode(file.getFileContent());
        var fileByte = FileUtil.decompressByte(compressedFileByte);
        return FileResponseDTO.builder()
                .id(file.getId())
                .fileByte(fileByte)
                .build();
    }
}
