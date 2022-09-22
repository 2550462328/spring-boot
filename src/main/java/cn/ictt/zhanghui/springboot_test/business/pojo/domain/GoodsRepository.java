package cn.ictt.zhanghui.springboot_test.business.pojo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/**
 * @author ZhangHui
 * @version 1.0
 * @className GoodsRepository
 * @description 这是描述信息
 * @date 2020/3/10
 */
@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer> {

    @Query(value = "select * from goods where id = ?1", nativeQuery = true)
    Goods findById(int id);

    //也可以通过注解的方式加上行级锁实现乐观锁
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select * from goods where id = ?1 for update", nativeQuery = true)
    Goods findByIdWithLock(int id);

    @Modifying
    @Query(value = "update goods set num = num - 1 where id = ?1 and num > 0", nativeQuery = true)
    void decreaseNumById(int id);

    @Modifying
    @Query(value = "update goods set num = num - 1,version = version + 1 where version = :#{#good.version} and  id = :#{#good.id}", nativeQuery = true)
    void updateNumById(@Param("good") Goods good);
}
