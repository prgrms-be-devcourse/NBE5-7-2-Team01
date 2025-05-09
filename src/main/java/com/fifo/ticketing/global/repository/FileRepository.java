package com.fifo.ticketing.global.repository;

import com.fifo.ticketing.global.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
