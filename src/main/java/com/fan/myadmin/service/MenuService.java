package com.fan.myadmin.service;

import com.fan.myadmin.entity.Menu;
import com.fan.myadmin.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fanweiwei
 * @create 2020-04-02 21:44
 */
@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> queryAll(){
        return menuRepository.findAll();
    }

    public List<Menu> findByPid(Long pid){
        return menuRepository.findByPid(pid);
    }
}
