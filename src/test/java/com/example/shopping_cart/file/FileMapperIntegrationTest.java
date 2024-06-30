package com.example.shopping_cart.file;

import com.example.shopping_cart.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class FileMapperIntegrationTest {

    private MultipartFile multipartFile;
    private List<MultipartFile> multipartFiles;
    @Autowired
    private FileMapper fileMapper;

    private static String content = "This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. ";

    @BeforeEach
    void setup() throws IOException {
//        String content = "This is a test file content.";
        String name = "test-file.txt";
        String originalFileName = "test-file.txt";
        String contentType = "text/plain";
        multipartFile = new MockMultipartFile(name, originalFileName, contentType, content.getBytes(StandardCharsets.UTF_8));
        multipartFiles = Collections.singletonList(multipartFile);


    }

    @Test
    void testToFile() {
        File file = FileMapper.toFile(multipartFile);
        assertEquals("test-file.txt", file.getName());
        assertEquals("text/plain", file.getFileType());
        assertEquals(BigInteger.valueOf(multipartFile.getSize()), file.getSize());
    }

    @Test
    void testToCompressedFileByteBase64() {
        byte[] fileBytes = "file content".getBytes();
        byte[] compressedBytes = FileMapper.toCompressedFileByteBase64(fileBytes);
        assertNotNull(compressedBytes);
    }

    @Test
    void testToFileResponseDTO() {
        File file = new File();
        file.setId(1L);
        file.setName("file");
        file.setFileType("text/plain");
        file.setSize(BigInteger.valueOf(1000));
        String encodedContent = Base64.getEncoder().encodeToString(content.getBytes());
        file.setFileContent(encodedContent.getBytes());
        // Set properties here
        FileResponseDTO fileResponseDTO = FileMapper.toFileResponseDTO(file);
        assertEquals(file.getId(), fileResponseDTO.getId());
        assertEquals(file.getName(), fileResponseDTO.getName());
        assertEquals(file.getFileType(), fileResponseDTO.getFileType());
        assertEquals(file.getSize(), fileResponseDTO.getSize());
    }

    @Test
    void testToFileSave() {
        Product product = new Product();
        product.setId(1L);
        // Set properties here
        File file = FileMapper.toFileSave(multipartFile, product);
        assertEquals("test-file.txt", file.getName());
        assertEquals("text/plain", file.getFileType());
        assertEquals(BigInteger.valueOf(multipartFile.getSize()), file.getSize());
        assertEquals(product, file.getProduct());
        assertEquals(product.getId(), file.getProduct().getId());
    }

    @Test
    void testToFileResponseDTOSave() {
        File file = new File();
        // Set properties here
        FileResponseDTO fileResponseDTO = FileMapper.toFileResponseDTOSave(file);
        assertEquals(file.getId(), fileResponseDTO.getId());
        assertEquals(file.getName(), fileResponseDTO.getName());
        assertEquals(file.getFileType(), fileResponseDTO.getFileType());
        assertEquals(file.getSize(), fileResponseDTO.getSize());
    }

    @Test
    void testToFileResponseDTOSearch() {
        // Create a File object
        File file = new File();
        file.setId(1L);
        file.setName("file");
        file.setFileType("text/plain");
        file.setSize(BigInteger.valueOf(1000));
        String encodedContent = Base64.getEncoder().encodeToString(content.getBytes());
        file.setFileContent(encodedContent.getBytes());

        // Call the toFileResponseDTOSearch function
        FileResponseDTO fileResponseDTO = FileMapper.toFileResponseDTOSearch(file);

        // Assert that the returned FileResponseDTO object has the expected properties
        assertEquals(file.getId(), fileResponseDTO.getId());
        assertEquals(file.getName(), fileResponseDTO.getName());
        assertEquals(file.getFileType(), fileResponseDTO.getFileType());
        assertEquals(file.getSize(), fileResponseDTO.getSize());
        byte[] fileContent = FileUtil.decompressByte(Base64.getDecoder().decode(file.getFileContent()));
        assertArrayEquals(fileContent, fileResponseDTO.getFileByte());
    }

    @Test
    void testToFileResponseDTOUpdate() {
        File file = new File();
        file.setId(1L);
        file.setName("file");
        file.setFileType("text/plain");
        file.setSize(BigInteger.valueOf(1000));
        String encodedContent = Base64.getEncoder().encodeToString(content.getBytes());
        file.setFileContent(encodedContent.getBytes()); // Set fileContent here
        // Set properties here
        FileResponseDTO fileResponseDTO = FileMapper.toFileResponseDTOUpdate(file);
        assertEquals(file.getId(), fileResponseDTO.getId());
        assertEquals(file.getName(), fileResponseDTO.getName());
        assertEquals(file.getFileType(), fileResponseDTO.getFileType());
        assertEquals(file.getSize(), fileResponseDTO.getSize());
    }

    @Test
    void testToFileResponseDTOUpdateProduct() {
        // Create a File object
        File file = new File();
        file.setId(1L);
        file.setName("file");
        file.setFileType("text/plain");
        file.setSize(BigInteger.valueOf(1000));
        String encodedContent = Base64.getEncoder().encodeToString(content.getBytes());
        file.setFileContent(encodedContent.getBytes());

        // Create a FileResponseDTO object
        FileResponseDTO fileResponseDTO = FileMapper.toFileResponseDTO(file);

        // Call the toFileResponseDTOUpdateProduct function
        FileResponseDTO result = FileMapper.toFileResponseDTOUpdateProduct(file, fileResponseDTO);

        // Assert that the returned FileResponseDTO object has the expected properties
        assertEquals(file.getId(), result.getId());
        assertEquals(file.getName(), result.getName());
        assertEquals(file.getFileType(), result.getFileType());
        assertEquals(file.getSize(), result.getSize());
        byte[] fileContent = FileUtil.decompressByte(Base64.getDecoder().decode(file.getFileContent()));
        assertArrayEquals(fileContent, result.getFileByte());
    }

    @Test
    void testToFileResponseDTOSaveProductFiles() {
        // Create a File object
        File file = new File();
        file.setId(1L);
        file.setName("file");
        file.setFileType("text/plain");
        file.setSize(BigInteger.valueOf(1000));
        byte[] contentBytes = content.getBytes();
        byte[] compressedContentBytes = FileUtil.compressByte(contentBytes);
        String encodedCompressedContent = Base64.getEncoder().encodeToString(compressedContentBytes);
        file.setFileContent(encodedCompressedContent.getBytes());

        // Create a list of FileResponseDTO objects
        List<FileResponseDTO> fileResponseDTOList = new ArrayList<>();
        FileResponseDTO fileResponseDTO = FileMapper.toFileResponseDTO(file);
        fileResponseDTOList.add(fileResponseDTO);

        // Call the toFileResponseDTOSaveProductFiles function
        FileResponseDTO result = FileMapper.toFileResponseDTOSaveProductFiles(file, fileResponseDTOList);

        // Assert that the returned FileResponseDTO object has the expected properties
        assertEquals(file.getId(), result.getId());
        assertEquals(file.getName(), result.getName());
        assertEquals(file.getFileType(), result.getFileType());
        assertEquals(file.getSize(), result.getSize());
        byte[] fileContent = FileUtil.decompressByte(Base64.getDecoder().decode(file.getFileContent()));
        assertArrayEquals(fileContent, result.getFileByte());
    }

    @Test
    void testToFileResponseDTOShoppingCart() {
        // Create a File object
        File file = new File();
        file.setId(1L);
        file.setName("file");
        file.setFileType("text/plain");
        file.setSize(BigInteger.valueOf(1000));
        String encodedContent = Base64.getEncoder().encodeToString(content.getBytes());
        file.setFileContent(encodedContent.getBytes());

        // Call the toFileResponseDTOShoppingCart function
        FileResponseDTO result = FileMapper.toFileResponseDTOShoppingCart(file);

        // Assert that the returned FileResponseDTO object has the expected properties
        assertEquals(file.getId(), result.getId());
        byte[] fileContent = FileUtil.decompressByte(Base64.getDecoder().decode(file.getFileContent()));
        assertArrayEquals(fileContent, result.getFileByte());
    }
}