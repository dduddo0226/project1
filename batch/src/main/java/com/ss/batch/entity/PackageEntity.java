package com.ss.batch.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "package")
public class PackageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packSeq;
    private String packageName;
    private Integer count;
    private Integer period;
	
}
