package org.tlum.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tlum.notification.constant.NotificationObject;
import org.tlum.notification.constant.NotificationType;

import java.time.Instant;

@Entity
@Table(name="notifications")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String message;

    @Column
    private Instant scheduledTime;

    @Column
    private boolean sent;

    @Column
    private String link;

    @Column
    private NotificationType type;

    @Column
    private NotificationObject object;
}
