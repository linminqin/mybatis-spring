package com.lmiky.test.jdp.service;

import javax.annotation.Resource;

import org.junit.Test;

import com.lmiky.jdp.service.BaseService;
import com.lmiky.test.BaseTest;

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
