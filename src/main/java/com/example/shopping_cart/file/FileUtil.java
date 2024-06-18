package com.example.shopping_cart.file;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.zip.*;

public class FileUtil {

    @NotNull
    public static byte[] compressByte(byte[] file) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(file);
        deflater.finish();

        ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
        byte[] dataHolder = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(dataHolder);
            stream.write(dataHolder, 0, size);
        }
        try {
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    @NotNull
    public static byte[] decompressByte(byte[] file) {
        Inflater inflater = new Inflater();
        inflater.setInput(file);

        ByteArrayOutputStream stream = new ByteArrayOutputStream(file.length);
        byte[] dataHolder = new byte[4 * 1024];

        try {
            while (!inflater.finished()) {
                int size = inflater.inflate(dataHolder);
                stream.write(dataHolder, 0, size);
            }
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream.toByteArray();

    }
}
