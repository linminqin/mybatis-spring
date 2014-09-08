package com.lmiky.test.jdp.database.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.lmiky.jdp.database.dao.BaseDAO;
import com.lmiky.jdp.database.model.PropertyCompareType;
import com.lmiky.jdp.database.model.PropertyFilter;
import com.lmiky.jdp.module.pojo.Module;
import com.lmiky.jdp.module.pojo.ModuleGroup;
import com.lmiky.test.BaseTest;
import com.lmiky.tiger.goods.pojo.Goods;

/**
 * DAO测试
 * @author lmiky
 * @date 2013-4-15
 */
public class DAOTest extends BaseTest{
	private BaseDAO baseDAO;
	
	@Test
	public void testGetDAO() {
		System.out.println(baseDAO);
	}
	
	@Test
	public void testFindById() {
		long begin = System.currentTimeMillis();
		Module module = baseDAO.find(Module.class, 290l);
		System.out.println(module);
		if(module != null) {
			System.out.println(module.getName());
		}
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}
	
	@Test
	public void testFindByProperty() {
		long begin = System.currentTimeMillis();
		Module module = baseDAO.find(Module.class, "path", "jdp/area");
		System.out.println(module);
		if(module != null) {
			System.out.println(module.getName());
		}
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}
	
	@Test
	public void testFindByProperties() {
		long begin = System.currentTimeMillis();
		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
		PropertyFilter propertyFilter = new PropertyFilter();
		propertyFilter.setCompareClass(Module.class);
		propertyFilter.setCompareType(PropertyCompareType.LLIKE);
		propertyFilter.setPropertyName("path");
		propertyFilter.setPropertyValue("jdp");
		propertyFilters.add(propertyFilter);
		PropertyFilter propertyFilter2 = new PropertyFilter();
		propertyFilter2.setCompareClass(Module.class);
		propertyFilter2.setCompareType(PropertyCompareType.LIKE);
		propertyFilter2.setPropertyName("name");
		propertyFilter2.setPropertyValue("地区");
		propertyFilters.add(propertyFilter2);
		Module module = baseDAO.find(Module.class, propertyFilters);
		System.out.println(module);
		if(module != null) {
			System.out.println(module.getId());
		}
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}

	@Test
	public void testFind() {
		long begin = System.currentTimeMillis();
		Module module = baseDAO.find(Module.class, 290l);
		System.out.println(module);
		if(module != null) {
			System.out.println(module.getName());
			ModuleGroup group = module.getGroup();
			System.out.println(group);
//			if(module.getGroup() != null) {
//				System.out.println(group.getName());
//				Set<Module> modules = module.getGroup().getModules();
//				if(modules != null) {
//					System.out.println(modules.size());
//					System.out.println(modules.iterator().next().getName());
//				}
//			}
		}
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}
	
	@Test
	public void testDeleteByPojo() {
		Module module = baseDAO.find(Module.class, 299l);
		baseDAO.delete(module);
	}
	
	@Test
	public void testDeleteById() {
		baseDAO.delete(Goods.class, 2417l);
	}
	
	@Test
	public void testDeleteByIds() {
		baseDAO.delete(Goods.class, new Long[] {1009l, 1008l});
	}
	
	@Test
	public void testDelete() {
		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
		PropertyFilter propertyFilter = new PropertyFilter();
		propertyFilter.setCompareClass(Goods.class);
		propertyFilter.setCompareType(PropertyCompareType.LLIKE);
		propertyFilter.setPropertyName("title");
		propertyFilter.setPropertyValue("测试添加1");
		propertyFilters.add(propertyFilter);
		PropertyFilter propertyFilter2 = new PropertyFilter();
		propertyFilter2.setCompareClass(Goods.class);
		propertyFilter2.setCompareType(PropertyCompareType.GT);
		propertyFilter2.setPropertyName("id");
		propertyFilter2.setPropertyValue(2400l);
		propertyFilters.add(propertyFilter2);
		baseDAO.delete(Goods.class, propertyFilters);
	}
	
	@Test
	public void testAdd() {
		Module module = new Module();
		module.setName("test");
		module.setPath("test");
		ModuleGroup group = new ModuleGroup();
		group.setId(132l);
		module.setGroup(group);
		baseDAO.add(module);
	}
	
	/**
	 * @param dao the dao to set
	 */
	@Resource(name="baseDAO")
	public void setDao(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
}
