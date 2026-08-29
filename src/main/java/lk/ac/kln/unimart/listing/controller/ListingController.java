package lk.ac.kln.unimart.listing.controller;

import jakarta.validation.Valid;
import lk.ac.kln.unimart.listing.ListingStatus;
import lk.ac.kln.unimart.listing.dto.ListingRequest;
import lk.ac.kln.unimart.listing.dto.ListingResponse;
import lk.ac.kln.unimart.listing.service.ListingService;
import lk.ac.kln.unimart.review.dto.ReviewResponse;
import lk.ac.kln.unimart.review.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/listings")
public class ListingController {

    private final ListingService service;
    private final ReviewService reviewService;

    public ListingController(ListingService service, ReviewService reviewService) {
        this.service = service;
        this.reviewService = reviewService;
    }

    @GetMapping
    public Page<ListingResponse> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) ListingStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.search(q, categoryId, status, pageable);
    }

    @GetMapping("/{id}")
    public ListingResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/{id}/reviews")
    public Page<ReviewResponse> getReviews(@PathVariable Long id, @PageableDefault(size = 10) Pageable pageable) {
        return reviewService.getForListing(id, pageable);
    }

    @PostMapping
    public ResponseEntity<ListingResponse> create(@Valid @RequestBody ListingRequest request,
                                                  Authentication authentication) {
        ListingResponse created = service.create(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ListingResponse update(@PathVariable Long id,
                                  @Valid @RequestBody ListingRequest request,
                                  Authentication authentication) {
        return service.update(id, request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication authentication) {
        service.archive(id, authentication.getName());
    }
}