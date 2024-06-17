package com.kltn.individualservice.entity;

import com.kltn.individualservice.dto.response.ObjectFileDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "document")
public class Document extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private Long objectId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String description;

    public Document(String type, Long objectId, ObjectFileDTO objectFileDTO) {
        this.type = type;
        this.objectId = objectId;
        this.fileName = objectFileDTO.getFileName();
        this.filePath = objectFileDTO.getFilePath();
        this.fileSize = objectFileDTO.getFileSize();
    }
}
