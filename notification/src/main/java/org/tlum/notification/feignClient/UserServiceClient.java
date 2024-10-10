package org.tlum.notification.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.tlum.notification.constant.NotificationObject;
import org.tlum.notification.dto.response.BaseResponseTemplate;

import java.util.List;

@FeignClient(name = "individual-service")
public interface UserServiceClient {

    @GetMapping(path = "/v1/users/by-object")
    BaseResponseTemplate<List<Long>> getUserIdByObject(@RequestHeader("Authorization") String token, @RequestParam NotificationObject object);
}
