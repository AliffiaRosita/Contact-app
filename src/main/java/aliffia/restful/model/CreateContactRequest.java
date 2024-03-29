package aliffia.restful.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateContactRequest {

    @JsonIgnore
    @NotBlank
    private String id;

    @NotBlank
    @Size(max=100)
    private String firstName;

    @Size(max=100)
    private String lastName;

    @Size(max=100)
    @Email
    private String email;

    @Size(max=100)
    private String phone;
}
