package com.pjt.refresheet.repository;

import com.pjt.refresheet.domain.SheetColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetColumnRepository extends JpaRepository<SheetColumn, Integer> {
}
