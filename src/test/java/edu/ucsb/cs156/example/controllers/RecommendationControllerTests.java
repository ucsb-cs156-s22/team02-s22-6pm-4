package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.Recommendation;
import edu.ucsb.cs156.example.repositories.RecommendationRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = RecommendationController.class)
@Import(TestConfig.class)
public class RecommendationControllerTests extends ControllerTestCase {
    @MockBean
    RecommendationRepository recommendationRepository;

    @MockBean
    Recommendation recommendation;

    // Authorization tests for /api/Recommendation/admin/all

    // @Test
    // public void logged_out_users_cannot_get_all() throws Exception {
    //     mockMvc.perform(get("/api/Recommendation/all")).andExpect(status().is(403)); // logged out users can't get all
    // }

    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void logged_in_users_can_get_all() throws Exception {
    //         mockMvc.perform(get("/api/Recommendation/all"))
    //                         .andExpect(status().is(200)); // logged
    // }

    // Authorization tests for /api/Recommendation/post
    // (Perhaps should also have these for put and delete)

    // @Test
    // public void logged_out_users_cannot_post() throws Exception {
    //         mockMvc.perform(post("/api/Recommendation/post"))
    //                         .andExpect(status().is(403));
    // }

    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void logged_in_regular_users_cannot_post() throws Exception {
    //         mockMvc.perform(post("/api/Recommendation/post"))
    //                         .andExpect(status().is(403)); // only admins can post
    // }
    // @WithMockUser(roles = { "USER" })
    // @Test
    // public void logged_in_user_can_get_all_Recommendation() throws Exception {

    //         // arrange
    //         LocalDate ldt1 = LocalDate.parse("2022-04-20");
    //         LocalDate ldt2 = LocalDate.parse("2022-05-01");

    //         Recommendation recommendation1 = Recommendation.builder()
    //             .requesterEmail("cgaucho@ucsb.edu")
    //             .professorEmail("phtcon@ucsb.edu")
    //             .explanation("BS/MS program")
    //             .dateRequested(ldt1)
    //             .dateNeeded(ldt2)
    //             .done(false)
    //             .build();

    //         LocalDate ldt3 = LocalDate.parse("2022-05-20");
    //         LocalDate ldt4 = LocalDate.parse("2022-11-15");

    //         Recommendation recommendation2 = Recommendation.builder()
    //             .requesterEmail("ldelplaya@ucsb.edu")
    //             .professorEmail("richert@ucsb.edu")
    //             .explanation("PhD CS Stanford")
    //             .dateRequested(ldt3)
    //             .dateNeeded(ldt4)
    //             .done(false)
    //             .build();

    //         ArrayList<Recommendation> expectedDates = new ArrayList<>();
    //         expectedDates.addAll(Arrays.asList(recommendation1, recommendation2));

    //         when(recommendationRepository.findAll()).thenReturn(expectedDates);

    //         // act
    //         MvcResult response = mockMvc.perform(get("/api/Recommendation/all"))
    //                         .andExpect(status().isOk()).andReturn();

    //         // assert

    //         verify(recommendationRepository, times(1)).findAll();
    //         String expectedJson = mapper.writeValueAsString(expectedDates);
    //         String responseString = response.getResponse().getContentAsString();
    //         assertEquals(expectedJson, responseString);
    // }

    // @WithMockUser(roles = { "ADMIN", "USER" })
    // @Test
    // public void an_admin_user_can_post_a_new_ucsbdate() throws Exception {
    //         // arrange

    //         LocalDate ldt1 = LocalDate.parse("2022-04-20");
    //         LocalDate ldt2 = LocalDate.parse("2022-05-01");

    //         Recommendation recommendation1 = Recommendation.builder()
    //                         .requesterEmail("cgaucho@ucsb.edu")
    //                         .professorEmail("phtcon@ucsb.edu")
    //                         .explanation("BS/MS program")
    //                         .dateRequested(ldt1)
    //                         .dateNeeded(ldt2)
    //                         .done(false)
    //                         .build();

    //         when(recommendationRepository.save(eq(recommendation1))).thenReturn(recommendation1);

    //         // act
    //         MvcResult response = mockMvc.perform(
    //                         post("/api/Recommendation/post?requesterEmail=cgaucho@ucsb.edu&professorEmail=phtcon@ucsb.edu&explanation=BS/MS%20program&dateRequested=2022-04-20&dateNeeded=2022-05-01&done=false")
    //                                         .with(csrf()))
    //                         .andExpect(status().isOk()).andReturn();

    //         // assert
    //         verify(recommendationRepository, times(1)).save(recommendation1);
    //         String expectedJson = mapper.writeValueAsString(recommendation1);
    //         String responseString = response.getResponse().getContentAsString();
    //         assertEquals(expectedJson, responseString);
    // }
}
