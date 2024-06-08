package com.example.shopping_cart.file;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Service
public class FileMapper {

    public File toFile(MultipartFile multipartFile) {
        File file = new File();
        file.setName(multipartFile.getOriginalFilename());
        file.setFileType(multipartFile.getContentType());
        System.out.println(file.getFileType());
        file.setSize(BigInteger.valueOf(multipartFile.getSize()));
        try {
            System.out.println(multipartFile.getBytes());
            var compressedFileByte = FileUtil.compressByte(multipartFile.getBytes());
            var compressedFileByteBase64 = Base64.encodeBase64(compressedFileByte, true);
            file.setFileContent(compressedFileByteBase64);
            System.out.println(compressedFileByteBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public FileResponseDTO toFileResponseDTO(File file) {
        var compressedFileByte = Base64.decodeBase64(file.getFileContent(), 0, file.getFileContent().length);
        var fileByte = FileUtil.decompressByte(compressedFileByte);
//        var fileByte = FileUtil.decompressFile(file.getFileContent());
//        System.out.println(fileByte);
        return new FileResponseDTO(
                file.getName(),
                file.getFileType(),
                file.getSize(),
                fileByte
        );
    }
}
