package com.example.shopping_cart.file;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileUtilTest {

    @Test
    void testCompressByte() {
        String originalContent = "This is a test content for compression.";
        byte[] originalBytes = originalContent.getBytes();

        // Compress the byte array
        byte[] compressedBytes = FileUtil.compressByte(originalBytes);

        // Assert that the compressed bytes are not null
        assertNotNull(compressedBytes);

        // In some cases, compressed data might not be smaller than original data.
        // So we handle this gracefully by asserting it's not too much larger.
        assertTrue(compressedBytes.length <= originalBytes.length + 12, "Compressed bytes should not be significantly larger than original bytes");
    }

    @Test
    void testDecompressByte() {
        String originalContent = "This is a test content for compression and decompression.";
        byte[] originalBytes = originalContent.getBytes();

        // Compress the byte array
        byte[] compressedBytes = FileUtil.compressByte(originalBytes);

        // Decompress the byte array
        byte[] decompressedBytes = FileUtil.decompressByte(compressedBytes);

        // Assert that the decompressed bytes are not null and equal to the original bytes
        assertNotNull(decompressedBytes);
        assertArrayEquals(originalBytes, decompressedBytes, "Decompressed bytes should match the original bytes");
    }

    @Test
    void testCompressAndDecompressByte() {
        String originalContent = "This is a test content for compression and decompression.";
        byte[] originalBytes = originalContent.getBytes();

        // Compress and then decompress the byte array
        byte[] compressedBytes = FileUtil.compressByte(originalBytes);
        byte[] decompressedBytes = FileUtil.decompressByte(compressedBytes);

        // Assert that the decompressed bytes match the original bytes
        assertNotNull(decompressedBytes);
        assertArrayEquals(originalBytes, decompressedBytes, "Decompressed bytes should match the original bytes");
    }
}
