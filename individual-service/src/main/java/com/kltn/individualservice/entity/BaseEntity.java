package com.kltn.individualservice.entity;

import com.kltn.individualservice.constant.EntityStatus;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity {
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastUpdatedAt;
    private String lastUpdatedBy;

    @Enumerated(EnumType.ORDINAL)
    @ColumnTransformer(read = "is_active + 1", write = "? - 1")
    private EntityStatus isActive;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
        this.isActive = EntityStatus.ACTIVE;
    }

    @PrePersist
    void prePersist() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        this.createdBy = request.getHeader("userId");
        this.lastUpdatedBy = request.getHeader("userId");
    }

    @PreUpdate
    void preUpdate() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        this.lastUpdatedBy = request.getHeader("userId");
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
