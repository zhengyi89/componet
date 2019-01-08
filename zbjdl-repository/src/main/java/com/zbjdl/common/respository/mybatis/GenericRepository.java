package com.zbjdl.common.respository.mybatis;

import java.util.List;

/**
 * title: 抽象 Repository 接口<br/>
 * description: 自定义Repository接口继承本接口后可以少些一些代码，并规范方法的命名<br/>
 * Copyright: Copyright (c)2014<br/>
 * Company: 云宝金服<br/>
 *
 */
public interface GenericRepository {

    <T> Integer save(T entity);

    <T> Integer batchSave(List<T> entities);

    <T> Integer update(T entity);

    <T> Integer batchUpdate(List<T> entities);

    Integer delete(Long id);

    <T> Integer batchDelete(List<T> id);

    Integer batchDeleteById(List<Long> id);

    <T> T selectById(Long id);
    /**
     * Retrieve a list of mapped objects by parameter.
     * @param parameter
     * @return
     */
    <T> List<T> findList(Object parameter);

    <T> List<T> findAll();


}
