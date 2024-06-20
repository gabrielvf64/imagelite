package io.github.dougllasfps.imageliteapi.domain.entity;

import io.github.dougllasfps.imageliteapi.domain.enums.ImageExtension;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String name;

    @Column
    private Long size;

    @Column
    @Enumerated(EnumType.STRING)
    private ImageExtension extension;

    @Column
    @CreatedDate
    private LocalDateTime uploadDate;

    @Column
    private String tags;

    @Lob
    @Column
    private byte[] file;
}