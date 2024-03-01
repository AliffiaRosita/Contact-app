package aliffia.restful.controller;

import aliffia.restful.entity.Address;
import aliffia.restful.entity.Contact;
import aliffia.restful.entity.User;
import aliffia.restful.model.*;
import aliffia.restful.repository.AddressRepository;
import aliffia.restful.repository.ContactRepository;
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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();

        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test",BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis()+ 1000000000);
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setId("test");
        contact.setUser(user);
        contact.setFirstName("Ita");
        contact.setEmail("ita@mail.com");
        contact.setLastName("Aliffia");
        contact.setPhone("082232344543");
        contactRepository.save(contact);
    }

//    @Test
//    void createAddressBadRequest() throws Exception{
//        CreateAddressRequest addressRequest = new CreateAddressRequest();
//        addressRequest.setCountry("");
//        mockMvc.perform(
//                post("/api/contacts/test/addresses")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(addressRequest))
//                        .header("X-API-TOKEN","test")
//        ).andExpectAll(
//                status().isBadRequest()
//        ).andDo(result -> {
//            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//            assertNotNull(response.getErrors());
//
//        });
//    }

    @Test
    void createAddressSuccess() throws Exception{
        CreateAddressRequest addressRequest = new CreateAddressRequest();
        addressRequest.setStreet("Jalan");
        addressRequest.setCountry("Indonesia");
        addressRequest.setCity("Samarinda");
        addressRequest.setPostalCode("70707");
        addressRequest.setProvince("Kaltim");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest))
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());


            assertEquals(addressRequest.getCity(), response.getData().getCity());
            assertEquals(addressRequest.getStreet(), response.getData().getStreet());
            assertEquals(addressRequest.getCountry(), response.getData().getCountry());
            assertEquals(addressRequest.getProvince(), response.getData().getProvince());
            assertEquals(addressRequest.getPostalCode(), response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));

        });
    }

    @Test
    void getAddressNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/123/addresses/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void getAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();

        Address  address = new Address();
        address.setId("test");
        address.setContact(contact);
        address.setStreet("Jalan");
        address.setCountry("Indonesia");
        address.setCity("Samarinda");
        address.setPostalCode("70707");
        address.setProvince("Kaltim");

        addressRepository.save(address);
        contactRepository.save(contact);
        mockMvc.perform(
                get("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(address.getId(),response.getData().getId());
            assertEquals(address.getCity(),response.getData().getCity());
            assertEquals(address.getStreet(),response.getData().getStreet());
            assertEquals(address.getProvince(),response.getData().getProvince());
            assertEquals(address.getCountry(),response.getData().getCountry());
            assertEquals(address.getPostalCode(),response.getData().getPostalCode());

        });
    }

//    @Test
//    void updateAddressBadRequest() throws Exception{
//
//        Contact contact = contactRepository.findById("test").orElseThrow();
//
//        Address  address = new Address();
//        address.setId("test");
//        address.setContact(contact);
//        address.setStreet("Jalan");
//        address.setCountry("Indonesia");
//        address.setCity("Samarinda");
//        address.setPostalCode("70707");
//        address.setProvince("Kaltim");
//
//        addressRepository.save(address);
//
//
//        UpdateAddressRequest addressRequest = new UpdateAddressRequest();
//        addressRequest.setCountry("");
//        mockMvc.perform(
//                put("/api/contacts/test/addresses/test")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(addressRequest))
//                        .header("X-API-TOKEN","test")
//        ).andExpectAll(
//                status().isBadRequest()
//        ).andDo(result -> {
//            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//            assertNotNull(response.getErrors());
//
//        });
//    }


    @Test
    void updateAddressSuccess() throws Exception{
        UpdateAddressRequest addressRequest = new UpdateAddressRequest();
        Contact contact = contactRepository.findById("test").orElseThrow();

        Address  address = new Address();
        address.setId("test");
        address.setContact(contact);
        address.setStreet("lama");
        address.setCountry("lama");
        address.setCity("lama");
        address.setPostalCode("4355");
        address.setProvince("lama");

        addressRepository.save(address);

        addressRequest.setStreet("Jalan");
        addressRequest.setCountry("Indonesia");
        addressRequest.setCity("Samarinda");
        addressRequest.setPostalCode("70707");
        addressRequest.setProvince("Kaltim");

        mockMvc.perform(
                put("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest))
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());


            assertEquals(addressRequest.getCity(), response.getData().getCity());
            assertEquals(addressRequest.getStreet(), response.getData().getStreet());
            assertEquals(addressRequest.getCountry(), response.getData().getCountry());
            assertEquals(addressRequest.getProvince(), response.getData().getProvince());
            assertEquals(addressRequest.getPostalCode(), response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));

        });
    }

    @Test
    void deleteAddressNotFound() throws Exception {
        mockMvc.perform(
                delete("/api/contacts/123/addresses/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void deleteAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();

        Address  address = new Address();
        address.setId("test");
        address.setContact(contact);
        address.setStreet("Jalan");
        address.setCountry("Indonesia");
        address.setCity("Samarinda");
        address.setPostalCode("70707");
        address.setProvince("Kaltim");

        addressRepository.save(address);
        contactRepository.save(contact);
        mockMvc.perform(
                delete("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals("OK",response.getData());

            assertFalse(addressRepository.existsById("test"));
        });
    }

    @Test
    void listAddressNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/123/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void listAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("test").orElseThrow();

        for (int i = 0; i < 5; i++) {
            Address  address = new Address();
            address.setId("test"+i);
            address.setContact(contact);
            address.setStreet("Jalan");
            address.setCountry("Indonesia");
            address.setCity("Samarinda");
            address.setPostalCode("70707");
            address.setProvince("Kaltim");
            addressRepository.save(address);
        }

        mockMvc.perform(
                get("/api/contacts/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<AddressResponse>> response=objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(5,response.getData().size());


        });
    }

}
