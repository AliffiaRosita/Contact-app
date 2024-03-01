package aliffia.restful.service;

import aliffia.restful.entity.Address;
import aliffia.restful.entity.Contact;
import aliffia.restful.entity.User;
import aliffia.restful.model.AddressResponse;
import aliffia.restful.model.CreateAddressRequest;
import aliffia.restful.model.UpdateAddressRequest;
import aliffia.restful.repository.AddressRepository;
import aliffia.restful.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request){
        validationService.validate(user);

        Contact contact = contactRepository.findFirstByUserAndId(user,request.getContactId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));
        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());

        addressRepository.save(address);

        return toAddressResponse(address);
    }

    @Transactional(readOnly=true)
    public AddressResponse get(User user, String contactId, String addressId){
        validationService.validate(user);
        Contact contact = contactRepository.findFirstByUserAndId(user,contactId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));

       Address address= addressRepository.findFirtByContactAndId(contact,addressId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));

        return toAddressResponse(address);

    }

    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request){
        validationService.validate(user);

        Contact contact = contactRepository.findFirstByUserAndId(user,request.getContactId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));
        Address address= addressRepository.findFirtByContactAndId(contact,request.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));

        address.setContact(contact);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());

        addressRepository.save(address);
        return toAddressResponse(address);
    }

    @Transactional
    public void delete (User user, String contactId, String addressId){
        validationService.validate(user);

        Contact contact = contactRepository.findFirstByUserAndId(user,contactId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));
        Address address= addressRepository.findFirtByContactAndId(contact,addressId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));

        addressRepository.delete(address);
    }

    public List<AddressResponse> list(User user, String contactId){
        validationService.validate(user);

        Contact contact = contactRepository.findFirstByUserAndId(user,contactId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));
        List<Address> addresses = addressRepository.findAllByContact(contact);
        return addresses.stream().map(this::toAddressResponse).toList();
    }

    private AddressResponse toAddressResponse(Address address){
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .country(address.getCountry())
                .province(address.getProvince())
                .postalCode(address.getPostalCode())
                .build();
    }
}
