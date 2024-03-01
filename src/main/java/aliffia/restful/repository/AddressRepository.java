package aliffia.restful.repository;

import aliffia.restful.entity.Address;
import aliffia.restful.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String > {

    Optional<Address> findFirtByContactAndId(Contact contact, String id);

    List<Address> findAllByContact(Contact contact);
}
