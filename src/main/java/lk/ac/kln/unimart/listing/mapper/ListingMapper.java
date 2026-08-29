package lk.ac.kln.unimart.listing.mapper;

import lk.ac.kln.unimart.listing.dto.ListingResponse;
import lk.ac.kln.unimart.listing.entity.Listing;
import org.springframework.stereotype.Component;

@Component
public class ListingMapper {

    public ListingResponse toResponse(Listing listing) {
        return new ListingResponse(
                listing.getId(),
                listing.getSeller().getId(),
                listing.getSeller().getFullName(),
                listing.getCategory().getId(),
                listing.getCategory().getName(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getPrice(),
                listing.getStatus(),
                listing.getCreatedAt(),
                listing.getUpdatedAt()
        );
    }
}