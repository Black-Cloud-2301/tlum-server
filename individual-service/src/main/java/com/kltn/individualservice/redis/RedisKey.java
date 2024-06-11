package com.kltn.individualservice.redis;

public enum RedisKey {
    USER("user"),
    ROLE("role"),
    PERMISSION("permission");

    private final String key;

    RedisKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
