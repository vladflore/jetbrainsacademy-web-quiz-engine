package vladflore.tech.webquizengine.model.dto;

import vladflore.tech.webquizengine.model.validator.EmailConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {

    private Long id;

    @NotNull
    @EmailConstraint
    private String email;

    @NotNull
    @Size(min = 5)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
