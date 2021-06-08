package com.springjpaboard.dto;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 테이블 매핑이 아닌 자식 Entity에 매핑 정도 상속하기 위한 애노테이션
@EntityListeners(AuditingEntityListener.class) //JPA에게 해당 Entity는 Auditing 기능을 사용하는 것을 알려주는 애노테이션
public class TimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
