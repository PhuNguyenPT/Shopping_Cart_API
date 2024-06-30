//package com.example.shopping_cart.file;
//
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
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class FileServiceTest {
//
//    @InjectMocks
//    private FileService fileService;
//
//    @Mock
//    private FileRepository fileRepository;
//
//    @Mock
//    private FileMapper fileMapper;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testSaveFiles() {
//        MultipartFile multipartFile = new MockMultipartFile("test", new byte[0]);
//        List<MultipartFile> multipartFiles = Collections.singletonList(multipartFile);
//        File file = new File();
//        when(fileMapper.toFile(multipartFile)).thenReturn(file);
//        when(fileRepository.save(file)).thenReturn(file);
//
//        ResponseEntity<?> response = fileService.saveFiles(multipartFiles);
//
//        verify(fileRepository, times(1)).save(file);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//    }
//
//    // Add more tests for other methods in FileService
//}