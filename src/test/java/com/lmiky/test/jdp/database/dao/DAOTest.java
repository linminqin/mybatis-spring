package com.lmiky.test.jdp.database.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.lmiky.jdp.database.dao.BaseDAO;
import com.lmiky.jdp.database.model.PropertyCompareType;
import com.lmiky.jdp.database.model.PropertyFilter;
import com.lmiky.jdp.module.pojo.Module;
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
		Goods goods = baseDAO.find(Goods.class, 508l);
		System.out.println(goods);
		if(goods != null) {
			System.out.println(goods.getTitle());
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
		propertyFilter.setCompareClass(Goods.class);
		propertyFilter.setCompareType(PropertyCompareType.EQ);
		propertyFilter.setPropertyName("title");
		propertyFilter.setPropertyValue("1222");
		propertyFilters.add(propertyFilter);
		PropertyFilter propertyFilter2 = new PropertyFilter();
		propertyFilter2.setCompareClass(Goods.class);
		propertyFilter2.setCompareType(PropertyCompareType.EQ);
		propertyFilter2.setPropertyName("id");
		propertyFilter2.setPropertyValue("2l");
		propertyFilters.add(propertyFilter2);
		Goods goods = baseDAO.find(Goods.class, propertyFilters);
		System.out.println(goods);
		if(goods != null) {
			System.out.println(goods.getTitle());
		}
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}

	@Test
	public void testDeleteByIds() {
		baseDAO.delete(Goods.class, new Long[] {1009l, 1008l});
	}
	
	/**
	 * @param dao the dao to set
	 */
	@Resource(name="baseDAO")
	public void setDao(BaseDAO baseDAO) {
		this.baseDAO = baseDAO;
	}
}
