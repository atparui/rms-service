package com.atparui.rmsservice.domain;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.springframework.stereotype.Component;

@Component
public class AppNavigationMenuCallback {

    @PostLoad
    @PostPersist
    @PostUpdate
    public void onAfterLoadOrSave(AppNavigationMenu entity) {
        entity.setIsPersisted();
    }
}
