package cn.ictt.zhanghui.springboot_test.domain;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@CacheConfig
public interface UserOpeareRepository extends CrudRepository<UserOperate, String> {
//    @Cacheable(value = "users",key = "#p0")
//    @Query(value = "select * from user_test where username=:name", nativeQuery = true)
//    UserOperate findByName(@Param("name") String name);
//
//    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
//    UserOperate findByNameAndAge(String name, Integer age);
//
//    @Query("from UserOperate u where u.name=:name")
//    UserOperate findUser(@Param("name") String name);
//
//    @Modifying
//    @CacheEvict(value = "users",allEntries = true, key = "#p0")
//    @Query(value = "update user u set u.age=?1 where u.id=?2", nativeQuery = true)
//    void editAgeById(Integer age, Long id);

    UserOperate findByUsername(@Param("name") String name);
}
