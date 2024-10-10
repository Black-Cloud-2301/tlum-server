package org.tlum.notification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GetNotificationsRequest extends PageableRequest {
    private String userId;
}
