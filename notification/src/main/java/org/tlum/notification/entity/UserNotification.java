package org.tlum.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_notifications")
@Getter
@Setter
@NoArgsConstructor
public class UserNotification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @ManyToOne
    private Notification notification;

    private boolean read;

    public UserNotification(Long userId, Notification notification) {
        this.userId = userId;
        this.notification = notification;
        this.read = false;
    }
}
