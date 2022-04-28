package edu.ucsb.cs156.example.controllers;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.ucsb.cs156.example.entities.Recommendation;
import edu.ucsb.cs156.example.repositories.RecommendationRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Api(description = "Recommendations")
@RequestMapping("/api/Recommendation")
@RestController
@Slf4j
public class RecommendationController extends ApiController {
    @Autowired
    RecommendationRepository recommendationRepository;

    @ApiOperation(value = "List all recommendations")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<Recommendation> allRecommendations() {
        Iterable<Recommendation> recommendations = recommendationRepository.findAll();
        return recommendations;
    }

    @ApiOperation(value = "Create a new recommendation")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/post")
    public Recommendation postRecommendation(
        @ApiParam("requesterEmail") @RequestParam String requesterEmail,
        @ApiParam("professorEmail") @RequestParam String professorEmail,
        @ApiParam("explanation") @RequestParam String explanation,
        @ApiParam("dateRequested") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateRequested,
        @ApiParam("dateNeeded") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateNeeded,
        @ApiParam("done") @RequestParam boolean done) throws JsonProcessingException {
        log.info("dateRequested={}", dateRequested);
        log.info("dateNeeded={}", dateNeeded);

        Recommendation recommendation = new Recommendation();
        recommendation.setRequesterEmail(requesterEmail);
        recommendation.setProfessorEmail(professorEmail);
        recommendation.setExplanation(explanation);
        recommendation.setDateRequested(dateRequested);
        recommendation.setDateNeeded(dateNeeded);
        recommendation.setDone(done);

        Recommendation savedRecommendation = recommendationRepository.save(recommendation);

        return savedRecommendation;
    }
}
