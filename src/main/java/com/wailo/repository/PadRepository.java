package com.wailo.repository;

import com.wailo.domain.Pad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PadRepository extends JpaRepository<Pad, Long> {}
