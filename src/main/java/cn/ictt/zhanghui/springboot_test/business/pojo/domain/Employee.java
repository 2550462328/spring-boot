package cn.ictt.zhanghui.springboot_test.business.pojo.domain;

import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * 员工
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/9/22 11:29
 **/
@NodeEntity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private String name;

    public Employee(String name) {
        this.name = name;
    }

    @Relationship(type = "TEAMMATE")
    private Set<Employee> teammates;

    public void worksWith(Employee employee){
        if(teammates == null){
            teammates = Sets.newHashSet();
        }
        teammates.add(employee);
    }

    @Override
    public String toString() {
        return this.name + "'s teammates => "
                + Optional.ofNullable(this.teammates).orElse(
                Collections.emptySet()).stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee() {
    }
}
