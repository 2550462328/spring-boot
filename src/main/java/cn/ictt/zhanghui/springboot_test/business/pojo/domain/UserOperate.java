package cn.ictt.zhanghui.springboot_test.business.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用spring_data_jpa操作的另一个user实体类
 */
@Entity
@Table(name = "user_test")
@Data
@AllArgsConstructor
@ToString
public class UserOperate implements Serializable {

    private static final long serialVersionUID = -1L;

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(length = 32)
    private String id;

    @NotNull
    @Column(length = 32, unique = true, nullable = false)
    private String username;

    @NotNull
    @Size(min = 6)
    @Column(length = 32, nullable = false)
    private String password;

    @Column(columnDefinition = "tinyint(1) default 1")
    private Boolean enabled;

    public UserOperate(){}

    public UserOperate(String username, String password) {
        this.username = username;
        this.password = password;
    }

//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private Role role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_test", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles = new ArrayList<>();

    public List<SimpleGrantedAuthority> getRoles() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode())));
        return authorities;
    }
}
