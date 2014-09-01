package com.lmiky.test.jdp.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.lmiky.jdp.service.BaseService;
import com.lmiky.jdp.service.exception.ServiceException;
import com.lmiky.test.BaseTest;
import com.lmiky.tiger.goods.pojo.Goods;

/**
 * @author lmiky
 * @date 2013-4-16
 */
public class ServiceTest extends BaseTest {
	private BaseService baseService;
	
	@Test
	public void testGetSevice() {
		System.out.println(baseService);
	}
	
	@Test
	public void testDeleteById() throws ServiceException {
		baseService.delete(Goods.class, 1900l);
	}
	
	@Test
	public void testDeleteByIds() throws ServiceException {
		baseService.delete(Goods.class, new Long[] {1919l, 1918L});
	}
	
	@Test
	public void testDelete() throws ServiceException {
		baseService.delete(Goods.class, "id", 2419l);
	}
	
	/**
	 * @return the baseService
	 */
	public BaseService getBaseService() {
		return baseService;
	}

	/**
	 * @param baseService the baseService to set
	 */
	@Resource(name="baseService")
	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
}
