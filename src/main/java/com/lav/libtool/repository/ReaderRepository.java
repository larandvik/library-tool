package com.lav.libtool.repository;

import com.lav.libtool.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRepository extends JpaRepository<Reader, Long> {

    boolean existsByEmail(String email);

}
