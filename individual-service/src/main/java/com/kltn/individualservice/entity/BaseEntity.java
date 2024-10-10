package com.kltn.individualservice.entity;

import com.kltn.individualservice.constant.EntityStatus;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity {
    private Instant createdAt;
    private String createdBy;
    private Instant lastUpdatedAt;
    private String lastUpdatedBy;
    private EntityStatus isActive;

    public BaseEntity() {
        this.createdAt = Instant.now();
        this.lastUpdatedAt = Instant.now();
        this.isActive = EntityStatus.ACTIVE;
    }

    @PrePersist
    void prePersist() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            this.createdBy = request.getHeader("userId");
            this.lastUpdatedBy = request.getHeader("userId");
        } else {
            this.createdBy = "system";
            this.lastUpdatedBy = "system";
        }
    }

    @PreUpdate
    void preUpdate() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            this.lastUpdatedBy = request.getHeader("userId");
        } else {
            this.lastUpdatedBy = "system";
        }
        this.lastUpdatedAt = Instant.now();
    }
}
