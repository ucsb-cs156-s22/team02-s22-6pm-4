package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.Article;
import edu.ucsb.cs156.example.repositories.ArticleRepository;

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

import java.time.LocalDateTime;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ArticleController.class)
@Import(TestConfig.class)
public class ArticleControllerTests extends ControllerTestCase {
    
    @MockBean
    ArticleRepository articleRepository;

    @MockBean
    UserRepository userRepository;

    // Authorization tests for /api/Article/admin/all

    @Test
        public void logged_out_users_cannot_get_all() throws Exception {
                mockMvc.perform(get("/api/Article/all"))
                                .andExpect(status().is(403)); // logged out users can't get all
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_users_can_get_all() throws Exception {
                mockMvc.perform(get("/api/Article/all"))
                                .andExpect(status().is(200)); // logged
        }

        @Test
        public void logged_out_users_cannot_get_by_id() throws Exception {
                mockMvc.perform(get("/api/Article?id=7"))
                                .andExpect(status().is(403)); // logged out users can't get by id
        }

        // Authorization tests for /api/Article/post
        // (Perhaps should also have these for put and delete)

        @Test
        public void logged_out_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/Article/post"))
                                .andExpect(status().is(403));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_regular_users_cannot_post() throws Exception {
                mockMvc.perform(post("/api/Article/post"))
                                .andExpect(status().is(403)); // only admins can post
        }

        // // Tests with mocks for database actions

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_exists() throws Exception {

                // arrange
                LocalDateTime ldt = LocalDateTime.parse("2022-01-03T00:00:00");

                Article article = Article.builder()
                                .title("Article Title")
                                .url("Article URL")
                                .explanation("Article Explanation")
                                .email("Article Email")
                                .dateAdded(ldt)
                                .build();

                when(articleRepository.findById(eq(7L))).thenReturn(Optional.of(article));

                // act
                MvcResult response = mockMvc.perform(get("/api/Article?id=7"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(articleRepository, times(1)).findById(eq(7L));
                String expectedJson = mapper.writeValueAsString(article);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void test_that_logged_in_user_can_get_by_id_when_the_id_does_not_exist() throws Exception {

                // arrange

                when(articleRepository.findById(eq(7L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(get("/api/Article?id=7"))
                                .andExpect(status().isNotFound()).andReturn();

                // assert

                verify(articleRepository, times(1)).findById(eq(7L));
                Map<String, Object> json = responseToJson(response);
                assertEquals("EntityNotFoundException", json.get("type"));
                assertEquals("Article with id 7 not found", json.get("message"));
        }

        @WithMockUser(roles = { "USER" })
        @Test
        public void logged_in_user_can_get_all_articles() throws Exception {

                // arrange
                LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                Article article1 = Article.builder()
                                .title("Article Title")
                                .url("Article URL")
                                .explanation("Article Explanation")
                                .email("Article Email")
                                .dateAdded(ldt1)
                                .build();

                LocalDateTime ldt2 = LocalDateTime.parse("2022-03-11T00:00:00");

                Article article2 = Article.builder()
                                .title("Article Title 2")
                                .url("Article URL 2")
                                .explanation("Article Explanation 2")
                                .email("Article Email 2")
                                .dateAdded(ldt2)
                                .build();

                ArrayList<Article> expectedArticles = new ArrayList<>();
                expectedArticles.addAll(Arrays.asList(article1, article2));

                when(articleRepository.findAll()).thenReturn(expectedArticles);

                // act
                MvcResult response = mockMvc.perform(get("/api/Article/all"))
                                .andExpect(status().isOk()).andReturn();

                // assert

                verify(articleRepository, times(1)).findAll();
                String expectedJson = mapper.writeValueAsString(expectedArticles);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void an_admin_user_can_post_a_new_article() throws Exception {
                // arrange

                LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                Article article1 = Article.builder()
                                .title("ArticleTitle")
                                .url("ArticleURL")
                                .explanation("ArticleExplanation")
                                .email("ArticleEmail")
                                .dateAdded(ldt1)
                                .build();

                when(articleRepository.save(eq(article1))).thenReturn(article1);

                // act
                MvcResult response = mockMvc.perform(
                                post("/api/Article/post?dateAdded=2022-01-03T00:00:00&email=ArticleEmail&explanation=ArticleExplanation&title=ArticleTitle&url=ArticleURL")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(articleRepository, times(1)).save(article1);
                String expectedJson = mapper.writeValueAsString(article1);
                String responseString = response.getResponse().getContentAsString();
                assertEquals(expectedJson, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_delete_an_article() throws Exception {
                // arrange

                LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                Article article1 = Article.builder()
                                .title("ArticleTitle")
                                .url("ArticleURL")
                                .explanation("ArticleExplanation")
                                .email("ArticleEmail")
                                .dateAdded(ldt1)
                                .build();

                when(articleRepository.findById(eq(15L))).thenReturn(Optional.of(article1));

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/Article?id=15")
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(articleRepository, times(1)).findById(15L);
                verify(articleRepository, times(1)).delete(any());

                Map<String, Object> json = responseToJson(response);
                assertEquals("Article with id 15 deleted", json.get("message"));
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_tries_to_delete_non_existant_article_and_gets_right_error_message()
                        throws Exception {
                // arrange

                when(articleRepository.findById(eq(15L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                delete("/api/Article?id=15")
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(articleRepository, times(1)).findById(15L);
                Map<String, Object> json = responseToJson(response);
                assertEquals("Article with id 15 not found", json.get("message"));
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_can_edit_an_existing_article() throws Exception {
                // arrange

                LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");
                LocalDateTime ldt2 = LocalDateTime.parse("2023-01-03T00:00:00");

                Article articleOrig = Article.builder()
                                .title("ArticleTitle")
                                .url("ArticleURL")
                                .explanation("ArticleExplanation")
                                .email("ArticleEmail")
                                .dateAdded(ldt1)
                                .build();

                Article articleEdited = Article.builder()
                                .title("NewArticleTitle")
                                .url("NewArticleURL")
                                .explanation("NewArticleExplanation")
                                .email("NewArticleEmail")
                                .dateAdded(ldt2)
                                .build();

                String requestBody = mapper.writeValueAsString(articleEdited);

                when(articleRepository.findById(eq(67L))).thenReturn(Optional.of(articleOrig));

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/Article?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isOk()).andReturn();

                // assert
                verify(articleRepository, times(1)).findById(67L);
                verify(articleRepository, times(1)).save(articleEdited); // should be saved with correct user
                String responseString = response.getResponse().getContentAsString();
                assertEquals(requestBody, responseString);
        }

        @WithMockUser(roles = { "ADMIN", "USER" })
        @Test
        public void admin_cannot_edit_article_that_does_not_exist() throws Exception {
                // arrange

                LocalDateTime ldt1 = LocalDateTime.parse("2022-01-03T00:00:00");

                Article editedArticle = Article.builder()
                                .title("ArticleTitle")
                                .url("ArticleURL")
                                .explanation("ArticleExplanation")
                                .email("ArticleEmail")
                                .dateAdded(ldt1)
                                .build();

                String requestBody = mapper.writeValueAsString(editedArticle);

                when(articleRepository.findById(eq(67L))).thenReturn(Optional.empty());

                // act
                MvcResult response = mockMvc.perform(
                                put("/api/Article?id=67")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .characterEncoding("utf-8")
                                                .content(requestBody)
                                                .with(csrf()))
                                .andExpect(status().isNotFound()).andReturn();

                // assert
                verify(articleRepository, times(1)).findById(67L);
                Map<String, Object> json = responseToJson(response);
                assertEquals("Article with id 67 not found", json.get("message"));

        }
}
