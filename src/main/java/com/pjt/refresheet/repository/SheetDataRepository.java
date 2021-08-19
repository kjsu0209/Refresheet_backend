package com.pjt.refresheet.repository;

import com.pjt.refresheet.domain.SheetData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetDataRepository extends JpaRepository<SheetData, String> {
}
