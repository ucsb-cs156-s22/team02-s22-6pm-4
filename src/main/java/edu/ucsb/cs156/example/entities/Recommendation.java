package edu.ucsb.cs156.example.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "recommendation")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String requesterEmail;
    private String professorEmail;
    private String explanation;
    private LocalDate dateRequested;
    private LocalDate dateNeeded;
    private boolean done;
}
