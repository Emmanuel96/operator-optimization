package com.wailo.repository;

import com.wailo.domain.OperationalItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Spring Data JPA repository for the OperationalItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperationalItemRepository extends JpaRepository<OperationalItem, Long> {
    Optional<OperationalItem> findByEzopsId(Long ezopsId);
}
