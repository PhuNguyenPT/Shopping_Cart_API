package com.example.shopping_cart.file;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Entity
//@PrimaryKeyJoinColumn(name = "file_id") -> only with join table strategy
//@DiscriminatorValue("F") -> only with single table strategy
public class File extends Resource {
    @Column(name = "file_type")
    private String fileType;
    @Column(name = "file_content")
    private byte[] fileContent;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
