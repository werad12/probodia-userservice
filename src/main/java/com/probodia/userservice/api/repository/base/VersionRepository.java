package com.probodia.userservice.api.repository.base;

import com.probodia.userservice.api.entity.base.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<AppVersion, Long> {

}
