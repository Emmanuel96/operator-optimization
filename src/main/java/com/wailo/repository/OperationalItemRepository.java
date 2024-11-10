package com.wailo.repository;

import com.wailo.domain.OperationalItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OperationalItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperationalItemRepository extends JpaRepository<OperationalItem, Long> {}
