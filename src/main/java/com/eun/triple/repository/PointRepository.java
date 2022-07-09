package com.eun.triple.repository;

import com.eun.triple.entity.Point;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointRepository extends JpaRepository<Point, UUID> {

    @Query("select DISTINCT p "
        + "from Point p left join fetch p.review r "
        + "WHERE r.userId = :userId "
        + "order by p.createDate desc")
    List<Point> findAllByUserIdOrderByCreateDateDesc(UUID userId);

}
