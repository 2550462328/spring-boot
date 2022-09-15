package cn.ictt.zhanghui.springboot_test.business.pojo.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author ZhangHui
 * @version 1.0
 * @className Goods
 * @description 这是描述信息
 * @date 2020/3/10
 */
@Table(name = "goods")
@Entity
@Data
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "num")
    private int num;

    @Column(name="version")
//    @Version  可以直接添加version注释，在进行update操作的时候会自动检测
    private int version;

    @Column(name = "price")
    private double price;

    @Column(name = "description")
    private String description;
}
