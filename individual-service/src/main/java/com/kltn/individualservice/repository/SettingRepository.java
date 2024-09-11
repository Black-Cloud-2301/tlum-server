package com.kltn.individualservice.repository;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.dto.request.SettingsRequest;
import com.kltn.individualservice.entity.Setting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {

    @Query("SELECT s FROM Setting s WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.tableCode} IS NULL OR s.tableCode = :#{#request.tableCode}) " +
            "AND (:#{#request.code} IS NULL OR s.code = :#{#request.code})")
    List<Setting> findAllByTableCode(SettingsRequest request);

    @Query("SELECT s FROM Setting s WHERE s.isActive IN :#{#request.entityStatuses} " +
            "AND (:#{#request.tableCode} IS NULL OR s.tableCode = :#{#request.tableCode}) " +
            "AND (:#{#request.code} IS NULL OR s.code = :#{#request.code})")
    Page<Setting> findAllByTableCode(SettingsRequest request, Pageable pageable);

    Optional<Setting> findByTableCodeAndCodeAndIsActive(String tableCode, String code, EntityStatus isActive);
}