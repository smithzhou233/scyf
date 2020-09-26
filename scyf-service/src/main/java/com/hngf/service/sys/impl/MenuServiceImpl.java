package com.hngf.service.sys.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;

import com.hngf.mapper.sys.MenuMapper;
import com.hngf.entity.sys.Menu;
import com.hngf.service.sys.MenuService;


@Service("MenuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper MenuMapper;


    @Override
	public List<Menu> getAll(Map<String, Object> params) {
    	return  MenuMapper.findList(params);
	}


	@Override
	public List<Menu> getAll() {
		return MenuMapper.findAll();
	}

    @Override
    public List<Menu> getByParentId(Long parentId) {
        return MenuMapper.findByParentId(parentId);
    }

    @Override
    public Menu getById(Long id){
        return MenuMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Menu Menu) {
        MenuMapper.add(Menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Menu Menu) {
        MenuMapper.update(Menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        MenuMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        MenuMapper.deleteById(id);
    }
}
