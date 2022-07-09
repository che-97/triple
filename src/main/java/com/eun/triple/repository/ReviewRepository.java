package com.eun.triple.repository;

import com.eun.triple.entity.Review;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query("select DISTINCT r from Review r left join r.photos left join fetch r.points WHERE r.id = :id ")
    Optional<Review> findById(UUID id);

    boolean existsByUserIdAndPlaceIdAndDeleteYn(UUID userId, UUID placeId, String deleteYn);

    boolean existsByPlaceIdAndDeleteYn(UUID placeId, String deleteYn);

}
