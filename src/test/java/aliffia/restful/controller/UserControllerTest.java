package aliffia.restful.controller;

import aliffia.restful.entity.User;
import aliffia.restful.model.RegisterUserRequest;
import aliffia.restful.model.UpdateUserRequest;
import aliffia.restful.model.UserResponse;
import aliffia.restful.model.WebResponse;
import aliffia.restful.repository.UserRepository;
import aliffia.restful.security.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() throws Exception{
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("rahasia");
        request.setName("Test");
        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result->{
            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertEquals("OK",response.getData());
        });
    }

    @Test
    void getUserUnauthorized() throws Exception{
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","notfound")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result->{
            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getUserUnauthorizedTokenNotSend() throws Exception{
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result->{
            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });
    }
    @Test
    void getUserSuccess() throws Exception{
        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("123123",BCrypt.gensalt()));
        user.setTokenExpiredAt(System.currentTimeMillis()+ 1000000000);
        userRepository.save(user);
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result->{
            WebResponse<UserResponse> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("Test",response.getData().getName());
            assertEquals("test",response.getData().getUsername());
        });
    }

    @Test
    void getUserTokenExpired() throws Exception{
        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("123123",BCrypt.gensalt()));
        user.setTokenExpiredAt(System.currentTimeMillis()- 1000000000);
        userRepository.save(user);
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result->{
            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void updateUserUnauthorized() throws Exception{
        UpdateUserRequest request = new UpdateUserRequest();
        mockMvc.perform(
                patch("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result->{
            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void updateUserSuccess() throws Exception{
        User user = new User();
        user.setUsername("test");
        user.setName("Test");
        user.setToken("test");
        user.setPassword(BCrypt.hashpw("123123",BCrypt.gensalt()));
        user.setTokenExpiredAt(System.currentTimeMillis()+ 1000000000);
        userRepository.save(user);
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Ita");
        request.setPassword("345345");
        mockMvc.perform(
                patch("/api/users/current")
                        .header("X-API-TOKEN","test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

        ).andExpectAll(
                status().isOk()
        ).andDo(result->{
            WebResponse<UserResponse> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("Ita",response.getData().getName());
            assertEquals("test",response.getData().getUsername());

            User userDb = userRepository.findById("test").orElse(null);
            assertNotNull(userDb);
            assertTrue(BCrypt.checkpw("345345", userDb.getPassword()));
        });
    }

}

