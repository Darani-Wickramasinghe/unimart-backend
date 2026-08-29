package lk.ac.kln.unimart.listing.repository;

import lk.ac.kln.unimart.listing.ListingStatus;
import lk.ac.kln.unimart.listing.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ListingRepository extends JpaRepository<Listing, Long>,
        JpaSpecificationExecutor<Listing> {
    Optional<Listing> findByIdAndStatusNot(Long id, ListingStatus status);
}