//package com.example.shopping_cart.file;
//
//import com.example.shopping_cart.product.Product;
//import jakarta.persistence.EntityNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.math.BigInteger;
//import java.nio.charset.StandardCharsets;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class FileServiceTest {
//
//    @Mock
//    private FileRepository fileRepository;
//
//    @Mock
//    private FileMapper fileMapper;
//
//    @InjectMocks
//    private FileService fileService;
//
//    private static String content = "This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. This is a test file content. ";
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void saveFiles_success() throws IOException {
//        String name = "test-file.txt";
//        String originalFileName = "test-file.txt";
//        String contentType = "text/plain";
//        MultipartFile multipartFile = new MockMultipartFile(name, originalFileName, contentType, content.getBytes(StandardCharsets.UTF_8));
//        List<MultipartFile> multipartFiles = Collections.singletonList(multipartFile);
//
//        File file = File.builder()
//                .name(multipartFile.getOriginalFilename())
//                .fileType(multipartFile.getContentType())
//                .size(BigInteger.valueOf(multipartFile.getSize()))
//                .fileContent(FileMapper.toCompressedFileByteBase64(multipartFile.getBytes()))
//                .build();
//
//        when(FileMapper.toFile(multipartFile)).thenReturn(file);
//        when(fileRepository.save(file)).thenReturn(file);
////        when(multipartFiles.isEmpty()).thenReturn(false);
//        when(FileMapper.toFile(multipartFile)).thenReturn(file);
//        when(fileRepository.save(file)).thenReturn(file);
//
////        List<MultipartFile> files = List.of(file);
//        ResponseEntity<?> response = fileService.saveFiles(multipartFiles);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertTrue(response.getBody().toString().contains("Saved 1 files successfully."));
//    }
//
////    @Test
////    void saveFilesByProduct_success() {
////        FileResponseDTO fileResponseDTO = FileResponseDTO.builder()
////                .id(1L)
////                .name("test")
////                .fileType("text/plain")
////                .size(BigInteger.valueOf(100))
////                .fileByte(new byte[]{})
////                .build();
////        MultipartFile file = mock(MultipartFile.class);
////        Product product = new Product();
////        when(file.isEmpty()).thenReturn(false);
////        when(FileMapper.toFileSave(file, product)).thenReturn(new File());
////        when(fileRepository.save(any(File.class))).thenReturn(new File());
////        when(FileMapper.toFileResponseDTOSave(any(File.class))).thenReturn(fileResponseDTO);
////
////        List<MultipartFile> files = List.of(file);
////        List<FileResponseDTO> response = fileService.saveFilesByProduct(product, files);
////
////        assertEquals(1, response.size());
////        assertEquals("Save null successfully", response.get(0).getMessage());
////    }
//
////    @Test
////    void findFiles_success() {
////        FileResponseDTO fileResponseDTO = FileResponseDTO.builder()
////                .id(1L)
////                .name("test")
////                .fileType("text/plain")
////                .size(BigInteger.valueOf(100))
////                .fileByte(new byte[]{})
////                .build();
////
////        String fileName = "test";
////        when(fileRepository.findByNameContainingIgnoreCase(fileName)).thenReturn(List.of(new File()));
////        when(FileMapper.toFileResponseDTO(any(File.class))).thenReturn(fileResponseDTO);
////
////        ResponseEntity<?> response = fileService.findFiles(fileName);
////
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////    }
//
////    @Test
////    void findFiles_notFound() {
////        String fileName = "test";
////        when(fileRepository.findByNameContainingIgnoreCase(fileName)).thenReturn(Collections.emptyList());
////
////        ResponseEntity<?> response = fileService.findFiles(fileName);
////
////        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
////        assertEquals("No files found with name: test", response.getBody());
////    }
//
//    @Test
//    void deleteFile_success() {
//        String fileName = "test";
//        when(fileRepository.deleteByName(fileName)).thenReturn(1L);
//
//        ResponseEntity<?> response = fileService.deleteFile(fileName);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue(response.getBody().toString().contains("Deleted 1 file(s) test successfully."));
//    }
//
////    @Test
////    void deleteFile_notFound() {
////        String fileName = "test";
////        when(fileRepository.deleteByName(fileName)).thenReturn(0L);
////
////        ResponseEntity<?> response = fileService.deleteFile(fileName);
////
////        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
////        assertTrue(response.getBody().toString().contains("No files found with name: test"));
////    }
////
////    @Test
////    void updateFile_success() throws IOException {
////        FileResponseDTO fileResponseDTO = FileResponseDTO.builder()
////                .id(1L)
////                .name("test")
////                .fileType("text/plain")
////                .size(BigInteger.valueOf(100))
////                .fileByte(new byte[]{})
////                .build();
////        MultipartFile file = mock(MultipartFile.class);
////        Product product = new Product();
////        Long fileId = 1L;
////        File existingFile = new File();
////        existingFile.setProduct(product);
////
////        when(fileRepository.findById(fileId)).thenReturn(existingFile);
////        when(file.getOriginalFilename()).thenReturn("test.txt");
////        when(file.getContentType()).thenReturn("text/plain");
////        when(file.getSize()).thenReturn(100L);
////        when(file.getBytes()).thenReturn(new byte[]{});
////        when(fileRepository.save(any(File.class))).thenReturn(existingFile);
////        when(FileMapper.toFileResponseDTOUpdate(any(File.class))).thenReturn(fileResponseDTO);
////
////        FileResponseDTO response = fileService.updateFile(file, product, fileId);
////
////        assertNotNull(response);
////    }
//
//    @Test
//    void updateFile_fileNotFound() {
//        MultipartFile file = mock(MultipartFile.class);
//        Product product = new Product();
//        Long fileId = 1L;
//
//        when(fileRepository.findById(fileId)).thenReturn(null);
//
//        assertThrows(EntityNotFoundException.class, () -> fileService.updateFile(file, product, fileId));
//    }
//
////    @Test
////    void saveAllFilesByProduct_success() {
////        MultipartFile file = mock(MultipartFile.class);
////        Product product = new Product();
////        when(file.isEmpty()).thenReturn(false);
////        when(FileMapper.toFileSave(file, product)).thenReturn(new File());
////        when(fileRepository.saveAll(anyList())).thenReturn(List.of(new File()));
////
////        List<MultipartFile> files = List.of(file);
////        List<File> savedFiles = fileService.saveAllFilesByProduct(files, product);
////
////        assertEquals(1, savedFiles.size());
////    }
//}
