package com.atparui.rmsservice.repository;

import com.atparui.rmsservice.domain.AppMenu;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data R2DBC repository for the AppMenu entity.
 */
@Repository
public interface AppMenuRepository extends R2dbcRepository<AppMenu, UUID> {
    Flux<AppMenu> findAllBy(Pageable pageable);
}
