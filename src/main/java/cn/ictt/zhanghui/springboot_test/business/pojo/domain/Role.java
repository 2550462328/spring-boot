package cn.ictt.zhanghui.springboot_test.business.pojo.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role_test")
@Data
public class Role implements Serializable {
    private static final long serialVersionUID = -1L;
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(length = 32)
    private String id;

    @Column(length = 32, unique = true, nullable = false)
    private String code;

    @Column(length = 32, unique = true, nullable = false)
    private String name;

    public Role(){}

    public Role(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission_test", joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private List<Permission> permissionList = new ArrayList<>();
}
