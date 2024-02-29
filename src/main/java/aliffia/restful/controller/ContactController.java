package aliffia.restful.controller;

import aliffia.restful.entity.User;
import aliffia.restful.model.ContactResponse;
import aliffia.restful.model.CreateContactRequest;
import aliffia.restful.model.WebResponse;
import aliffia.restful.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.Media;

@RestController
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse>create(User user, @RequestBody CreateContactRequest request){
        ContactResponse contactResponse = contactService.create(user,request);
        return WebResponse.<ContactResponse>builder()
                .data(contactResponse)
                .build();
    }
}
