package com.dofuspulse.api.auth;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

//    @Test
//    public void shouldCreateUserAndReturn200WhenValidRegistrationDataIsProvided() throws Exception {
//
//        RegisterRequest registerRequest = RegisterRequest.builder()
//                .email("newacc@gmail.com")
//                .password("ValidPassword123@".toCharArray())
//                .role(Role.USER)
//                .build();
//
//        this.mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
//                .andExpect(status().isOk())
//                .andReturn();
//    }

  @Test
  void shouldReturn400WhenInvalidRegistrationDataIsProvided() throws Exception {

    RegisterRequest registerRequest = RegisterRequest.builder()
        .email("invalidmail")
        .password("invalidpassword".toCharArray())
        .role(Role.USER)
        .build();

    this.mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(registerRequest)))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

  @Test
  void shouldReturn200WhenUserLogsInWithValidCredentials() throws Exception {

    this.mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(LoginRequest.builder()
                .email("newuser@test.com")
                .password("Notvalidpass123@".toCharArray()).build())))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  void shouldReturn401WhenLoginFailsWithInvalidCredentials() throws Exception {

    this.mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(LoginRequest.builder()
                .email("newuser@test.com")
                .password("invalidpassword".toCharArray()).build())))
        .andExpect(status().isUnauthorized())
        .andReturn();
  }

  @Test
  @WithUserDetails("usertest@test.com")
  void shouldReturn403WhenNormalUserAccessesAdminProtectedResource() throws Exception {

    this.mockMvc.perform(get("/admin")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithUserDetails("admin@admin.com")
  void shouldReturnSuccessWhenAdminAccessesAdminProtectedResource() throws Exception {

    this.mockMvc.perform(get("/admin")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }
}
