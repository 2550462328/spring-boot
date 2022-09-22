package cn.ictt.zhanghui.springboot_test.business.pojo.domain;

import cn.ictt.zhanghui.springboot_test.business.pojo.dto.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.naming.Name;

@Repository
public interface PersonRepository extends CrudRepository<Person, Name> {

}
