package aliffia.restful.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateContactRequest {
    @NotBlank
    private String id;

    @NotBlank
    @Size(max=100)
    private String firstName;

    @Size(max=100)
    private String lastName;

    @Email
    @Size(max=100)
    private String email;

    @Size(max=100)
    private String phone;
}
