package lk.ac.kln.unimart.review.service;

import lk.ac.kln.unimart.common.exception.ConflictException;
import lk.ac.kln.unimart.common.exception.ForbiddenException;
import lk.ac.kln.unimart.common.exception.ResourceNotFoundException;
import lk.ac.kln.unimart.order.OrderStatus;
import lk.ac.kln.unimart.order.entity.Order;
import lk.ac.kln.unimart.order.repository.OrderRepository;
import lk.ac.kln.unimart.review.dto.ReviewCreateRequest;
import lk.ac.kln.unimart.review.dto.ReviewResponse;
import lk.ac.kln.unimart.review.dto.ReviewUpdateRequest;
import lk.ac.kln.unimart.review.entity.Review;
import lk.ac.kln.unimart.review.mapper.ReviewMapper;
import lk.ac.kln.unimart.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ReviewService {

    private final ReviewRepository reviews;
    private final OrderRepository orders;
    private final ReviewMapper mapper;

    public ReviewService(ReviewRepository reviews, OrderRepository orders, ReviewMapper mapper) {
        this.reviews = reviews;
        this.orders = orders;
        this.mapper = mapper;
    }

    @Transactional
    public ReviewResponse create(ReviewCreateRequest request, String email) {
        Order order = orders.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new ConflictException("Only completed orders can be reviewed");
        }
        if (!order.getBuyer().getUniversityEmail().equalsIgnoreCase(email)) {
            throw new ForbiddenException("Only the buyer can review this order");
        }
        if (reviews.existsByOrderId(order.getId())) {
            throw new ConflictException("This order already has a review");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setReviewer(order.getBuyer());
        review.setReviewee(order.getListing().getSeller());
        review.setRating(request.rating());
        review.setComment(normalize(request.comment()));
        review.setCreatedAt(Instant.now());
        review.setUpdatedAt(Instant.now());

        return mapper.toResponse(reviews.save(review));
    }

    @Transactional(readOnly = true)
    public ReviewResponse get(Long id) {
        Review review = reviews.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        return mapper.toResponse(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getForListing(Long listingId, Pageable pageable) {
        return reviews.findByOrder_Listing_Id(listingId, pageable).map(mapper::toResponse);
    }

    @Transactional
    public ReviewResponse update(Long id, ReviewUpdateRequest request, String email) {
        Review review = requireOwnedReview(id, email);
        review.setRating(request.rating());
        review.setComment(normalize(request.comment()));
        review.setUpdatedAt(Instant.now());
        return mapper.toResponse(review);
    }

    @Transactional
    public void delete(Long id, String email) {
        Review review = requireOwnedReview(id, email);
        reviews.delete(review);
    }

    private Review requireOwnedReview(Long id, String email) {
        Review review = reviews.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (!review.getReviewer().getUniversityEmail().equalsIgnoreCase(email)) {
            throw new ForbiddenException("You do not own this review");
        }
        return review;
    }

    private String normalize(String comment) {
        return comment == null ? null : comment.trim();
    }
}