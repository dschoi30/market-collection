package com.marketcollection.domain.point.repository;

import com.marketcollection.domain.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
