package com.atparui.rmsservice.domain;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.springframework.stereotype.Component;

@Component
public class AuthorityCallback {

    @PostLoad
    @PostPersist
    @PostUpdate
    public void onAfterLoadOrSave(Authority entity) {
        entity.setIsPersisted();
    }
}
