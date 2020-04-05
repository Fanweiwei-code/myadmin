package com.fan.myadmin.repository;

import com.fan.myadmin.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fanweiwei
 * @create 2020-04-02 21:44
 */
public interface MenuRepository extends JpaRepository<Menu,Long> , JpaSpecificationExecutor<Menu> {
    /**
     * 根据菜单名称查询
     * @param name 菜单名称
     * @return /
     */
    @Query("from Menu where name =?1")
    Menu findByName(String name);


    /**
     * 根据菜单的 PID 查询
     * @param pid /
     * @return /
     */
    @Query("from Menu where pid =?1")
    List<Menu> findByPid(long pid);

    /**
     * 根据菜单的 ID 查询
     * @param id /
     * @return /
     */
    @Query("from Menu where id =?1")
    Menu findById(long id);

}
