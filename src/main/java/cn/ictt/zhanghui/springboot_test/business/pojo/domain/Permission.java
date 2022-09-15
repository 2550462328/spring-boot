package cn.ictt.zhanghui.springboot_test.business.pojo.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "permission_test")
@Data
public class Permission implements Serializable {
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

    @Column(length = 32)
    private String parentId;
}
