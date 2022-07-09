package com.eun.triple.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class Base implements Serializable {

    @CreatedDate
    @Column(updatable = false, columnDefinition = "DATETIME default CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime updateDate;


}
