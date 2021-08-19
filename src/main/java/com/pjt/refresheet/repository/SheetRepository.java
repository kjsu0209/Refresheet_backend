package com.pjt.refresheet.repository;

import com.pjt.refresheet.domain.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends JpaRepository<Sheet, String> {
}
