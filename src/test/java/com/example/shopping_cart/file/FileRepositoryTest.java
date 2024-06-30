package com.example.shopping_cart.file;

import com.example.shopping_cart.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FileRepository fileRepository;

    private File file;

    @BeforeEach
    public void setup() {
        file = new File();
        file.setName("testFile");
        entityManager.persist(file);
        entityManager.flush();
    }

    @Test
    public void whenFindByName_thenReturnFile() {
        List<File> found = fileRepository.findByNameContainingIgnoreCase(file.getName());
        assertThat(found.get(0).getName()).isEqualTo(file.getName());
    }

    @Test
    public void whenInvalidName_thenReturnEmptyList() {
        List<File> fromDb = fileRepository.findByNameContainingIgnoreCase("doesNotExist");
        assertThat(fromDb).isEmpty();
    }

    @Test
    public void givenSetOfFiles_whenFindAll_thenReturnAllFiles() {
        File file2 = new File();
        file2.setName("file2");
        entityManager.persist(file2);
        entityManager.flush();

        List<File> allFiles = fileRepository.findAll();
        assertThat(allFiles).hasSize(2).extracting(File::getName).containsOnly(file.getName(), file2.getName());
    }

    @Test
    public void whenFindById_thenReturnFile() {
        File found = fileRepository.findById(file.getId());
        assertThat(found.getName()).isEqualTo(file.getName());
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        File fromDb = fileRepository.findById(-11L);
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenDeleteByName_thenDeletingShouldBeSuccessful() {
        fileRepository.deleteByName(file.getName());
        assertThat(fileRepository.findByNameContainingIgnoreCase(file.getName())).isEmpty();
    }
}

//package com.example.shopping_cart.file;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.mockito.ArgumentMatchers.anyList;
//
//@SpringBootTest
//public class FileRepositoryTest {
//
//    @MockBean
//    private FileRepository fileRepository;
//
//    @MockBean
//    private File file;
//
//    @BeforeEach
//    public void setup() {
//        File mockFile = new File();
//        mockFile.setId(1L);
//        mockFile.setName("file");
//        when(fileRepository.findById(1L)).thenReturn(mockFile);
//    }
//
//    @Test
//    public void testFindById() {
//        Optional<File> optionalFile = Optional.ofNullable(fileRepository.findById(1L));
//        assertTrue(optionalFile.isPresent());
//    }
//
//    @Test
//    public void testSave() {
//        when(fileRepository.save(any(File.class))).thenReturn(file);
//        File savedFile = fileRepository.save(file);
//        assertNotNull(savedFile);
//    }
//
//    @Test
//    public void testSaveAll() {
//        when(fileRepository.saveAll(anyList())).thenReturn(Arrays.asList(file));
//        List<File> savedFiles = fileRepository.saveAll(Arrays.asList(file));
//        assertFalse(savedFiles.isEmpty());
//    }
//
//    @Test
//    public void testFindByNameContainingIgnoreCase() {
//        when(fileRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(file));
//        List<File> files = fileRepository.findByNameContainingIgnoreCase("file");
//        assertFalse(files.isEmpty());
//    }
//
//    @Test
//    public void testDeleteByName() {
////        doNothing().when(fileRepository).deleteByName(anyString());
//        fileRepository.deleteByName("file");
//        verify(fileRepository, times(1)).deleteByName("file");
//    }
//}