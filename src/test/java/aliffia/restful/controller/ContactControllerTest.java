package aliffia.restful.controller;

import aliffia.restful.entity.Contact;
import aliffia.restful.entity.User;
import aliffia.restful.model.ContactResponse;
import aliffia.restful.model.CreateContactRequest;
import aliffia.restful.model.WebResponse;
import aliffia.restful.repository.ContactRepository;
import aliffia.restful.repository.UserRepository;
import aliffia.restful.security.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void SetUp(){
        contactRepository.deleteAll();
        userRepository.deleteAll();
        User user = new User();

        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test",BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis()+ 1000000000);
        userRepository.save(user);
    }

//    @Test
//    void createContactBadRequest() throws Exception {
//        CreateContactRequest request = new CreateContactRequest();
//        request.setFirstName("");
//        request.setEmail("salah");
//
//        mockMvc.perform(
//                post("/api/contacts")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("X-API-TOKEN","test")
//        ).andExpectAll(
//                status().isBadRequest()
//        ).andDo(result -> {
//            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
//            });
//            assertNotNull(response.getErrors());
//        });
//    }

@Test
void createContactSuccess() throws Exception {
    CreateContactRequest request = new CreateContactRequest();
    request.setFirstName("Ita");
    request.setEmail("ita@mail.com");
    request.setLastName("Aliffia");
    request.setPhone("082232344543");

    mockMvc.perform(
            post("/api/contacts")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-API-TOKEN","test")
    ).andExpectAll(
            status().isOk()
    ).andDo(result -> {
        WebResponse<ContactResponse> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNull(response.getErrors());
        assertEquals("Ita",response.getData().getFirstName());
        assertEquals("ita@mail.com",response.getData().getEmail());
        assertEquals("Aliffia",response.getData().getLastName());
        assertEquals("082232344543",response.getData().getPhone());

         assertTrue(contactRepository.existsById(response.getData().getId()));
    });
}

}
