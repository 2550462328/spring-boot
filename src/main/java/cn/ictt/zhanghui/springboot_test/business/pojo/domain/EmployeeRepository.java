package cn.ictt.zhanghui.springboot_test.business.pojo.domain;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/9/22 12:39
 **/
public interface EmployeeRepository extends Neo4jRepository<Employee, Long> {

    Employee findByName(String name);

    List<Employee> findByTeammatesName(String name);
}
