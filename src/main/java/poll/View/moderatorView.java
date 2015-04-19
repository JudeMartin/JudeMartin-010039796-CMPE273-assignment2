package poll.View;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

public class moderatorView {

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ'Z'");
    @NotNull
    @Size(min = 1)
    public String name;
    @NotNull
    @Size(min = 1)
    @NotEmpty
    public String email;
    @NotNull
    @Size(min = 1)
    public String password;
    public Date created_at;
    @Id
    public Integer id;

    public moderatorView() {
        super();
        this.created_at = new Date(System.currentTimeMillis());
    }

    public moderatorView(String name, String email, String password) {
        this();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCreated_at() {
        return df.format(created_at);
    }

}