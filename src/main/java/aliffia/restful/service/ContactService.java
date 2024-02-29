package aliffia.restful.service;

import aliffia.restful.entity.Contact;
import aliffia.restful.entity.User;
import aliffia.restful.model.ContactResponse;
import aliffia.restful.model.CreateContactRequest;
import aliffia.restful.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ValidationService validationService;
    @Transactional
    public ContactResponse create(User user,CreateContactRequest request){
        validationService.validate(user);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setUser(user);
        contactRepository.save(contact);

        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    @Transactional(readOnly=true)
    public ContactResponse get(User user, String id){
        Contact contact = contactRepository.findFirstByUserAndId(user,id)
                .orElse()

    }
}
