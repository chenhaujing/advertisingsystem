package com.sztouyun.advertisingsystem.repository.system;

import com.sztouyun.advertisingsystem.model.system.Menu;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by szty on 2017/7/25.
 */
public interface MenuRepository extends BaseRepository<Menu> {

    Menu findById(String id);

    Integer deleteById(String id);

    Page<Menu> findAll(Pageable pageable);

}
