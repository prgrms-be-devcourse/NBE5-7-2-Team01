package com.fifo.ticketing.global.entity;

import com.fifo.ticketing.domain.performance.entity.Performance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
public class File extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String encodedFileName;

    private String originalFileName;

}
