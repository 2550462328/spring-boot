package cn.ictt.zhanghui.springboot_test.common.dynamicquery;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * JPA nativeSql执行的方法类
 * 核心是 EntityManager.createNativeQuery(sql)
 * 除此之外如果想映射具体实体类或者复杂对象 可以使用 EntityManager.createNamedQuery
 *
 * createNamedQuery需要借助 @NamedNativeQuery映射实体类方法（执行sql语句）
 * 借助@SqlResultSetMapping 映射返回类型（单独封装一个实体做返回实体类）
 *
 */
@Repository("dynamicQuery")
public class DynamicQueryImpl implements DynamicQuery {

    Logger logger = LoggerFactory.getLogger(DynamicQueryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void save(Object entity) {
        em.persist(entity);
    }

    @Override
    public void update(Object entity) {
        em.merge(entity);
    }

    @Override
    public <T> void delete(Class<T> entityClass, Object entityid) {
        delete(entityClass, new Object[]{entityid});
    }

    @Override
    public <T> void delete(Class<T> entityClass, Object[] entityids) {
        for (Object id : entityids) {
            em.remove(em.getReference(entityClass, id));
        }
    }

    private Query createNativeQuery(String sql, Object... params) {
        Query q = em.createNativeQuery(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                // 与Hibernate不同,jpa query从位置1开始
                q.setParameter(i + 1, params[i]);
            }
        }

        return q;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> nativeQueryList(String nativeSql, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        q.unwrap(NativeQuery.class).setResultTransformer(Transformers.TO_LIST);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> nativeQueryListModel(Class<T> resultClass,
                                            String nativeSql, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        q.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(resultClass));
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> nativeQueryListMap(String nativeSql, Object... params) {
        Query q = createNativeQuery(nativeSql, params);
        q.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q.getResultList();
    }

    @Override
    public Object nativeQueryObject(String nativeSql, Object... params) {
        return createNativeQuery(nativeSql, params).getSingleResult();
    }

    @Override
    public int nativeExecuteUpdate(String nativeSql, Object... params) {
        return createNativeQuery(nativeSql, params).executeUpdate();
    }

    @Override
    public Object[] nativeQueryArray(String nativeSql, Object... params) {
        return (Object[]) createNativeQuery(nativeSql, params).getSingleResult();
    }

}
