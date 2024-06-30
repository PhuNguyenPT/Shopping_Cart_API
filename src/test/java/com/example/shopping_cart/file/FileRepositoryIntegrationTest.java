package com.example.shopping_cart.file;

import com.example.shopping_cart.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class FileRepositoryIntegrationTest {

    @Autowired
    private FileRepository fileRepository;

    private File file;

    @BeforeEach
    public void setup() {
        fileRepository.deleteAll();

        file = new File();
        file.setName("testFile");
        fileRepository.save(file);
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
        fileRepository.save(file2);

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