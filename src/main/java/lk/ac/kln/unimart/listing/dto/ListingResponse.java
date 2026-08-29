package lk.ac.kln.unimart.listing.dto;

import lk.ac.kln.unimart.listing.ListingStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record ListingResponse(
        Long id,
        Long sellerId,
        String sellerName,
        Long categoryId,
        String categoryName,
        String title,
        String description,
        BigDecimal price,
        ListingStatus status,
        Instant createdAt,
        Instant updatedAt
) {}