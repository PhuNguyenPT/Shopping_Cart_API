package com.example.shopping_cart.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyList;

@SpringBootTest
public class FileRepositoryTest2 {

    @MockBean
    private FileRepository fileRepository;

    @MockBean
    private File file;

    @BeforeEach
    public void setup() {
        File mockFile = new File();
        mockFile.setId(1L);
        mockFile.setName("file");
        when(fileRepository.findById(1L)).thenReturn(mockFile);
    }

    @Test
    public void testFindById() {
        Optional<File> optionalFile = Optional.ofNullable(fileRepository.findById(1L));
        assertTrue(optionalFile.isPresent());
    }

    @Test
    public void testSave() {
        when(fileRepository.save(any(File.class))).thenReturn(file);
        File savedFile = fileRepository.save(file);
        assertNotNull(savedFile);
    }

    @Test
    public void testSaveAll() {
        when(fileRepository.saveAll(anyList())).thenReturn(Arrays.asList(file));
        List<File> savedFiles = fileRepository.saveAll(Arrays.asList(file));
        assertFalse(savedFiles.isEmpty());
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        when(fileRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(file));
        List<File> files = fileRepository.findByNameContainingIgnoreCase("file");
        assertFalse(files.isEmpty());
    }

    @Test
    public void testDeleteByName() {
//        doNothing().when(fileRepository).deleteByName(anyString());
        fileRepository.deleteByName("file");
        verify(fileRepository, times(1)).deleteByName("file");
    }
}