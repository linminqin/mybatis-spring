package com.lmiky.jdp.database.dao.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Table;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.lmiky.jdp.database.dao.BaseDAO;
import com.lmiky.jdp.database.exception.DatabaseException;
import com.lmiky.jdp.database.model.PropertyCompareType;
import com.lmiky.jdp.database.model.PropertyFilter;
import com.lmiky.jdp.database.model.Sort;
import com.lmiky.jdp.database.pojo.BasePojo;

/**
 * 基础dao
 * @author lmiky
 * @date 2013-4-15
 */
@Repository("baseDAO")
public class BaseDAOImpl implements BaseDAO {
	
	//参数字段
	/**
	 * 过滤条件
	 */
	protected static final String PARAM_NAME_FILTERS = "filters";
	/**
	 * 排序
	 */
	protected static final String PARAM_NAME_SORTS = "sorts";
	/**
	 * 分页：起始位置
	 */
	protected static final String PARAM_NAME_PAGE_FIRST = "pageFirst";
	/**
	 * 分页：查询记录数
	 */
	protected static final String PARAM_NAME_PAGE_SIZE = "pageSize";
	
	//映射文件命名空间
	/**
	 * 公共映射
	 */
	protected static final String MAPPER_NAMESPACE_COMMON = "common";
	
	//sql方法名
	/**
	 * sql方法名：查询
	 */
	protected static final String SQLNAME_FIND = "find";
	
	/**
	 * sql方法名：添加
	 */
	protected static final String SQLNAME_ADD = "add";
	
	/**
	 * sql方法名：修改
	 */
	protected static final String SQLNAME_UPDATE = "update";
	
	/**
	 * sql方法名：获取列表
	 */
	protected static final String SQLNAME_LIST = "list";
	
	/**
	 * sql方法名：统计
	 */
	protected static final String SQLNAME_COUNT = "count";
	
	//查询方法名后缀
	/**
	 * 查询方法名后缀：查询
	 */
	protected static final String SQLNAME_SUFFIX_FIND = "." + SQLNAME_FIND;
	/**
	 * 查询方法名后缀：添加
	 */
	protected static final String SQLNAME_SUFFIX_ADD = "." + SQLNAME_ADD;
	/**
	 * 查询方法名后缀：添加
	 */
	protected static final String SQLNAME_SUFFIX_UPDATE = "." + SQLNAME_UPDATE;
	/**
	 * 查询方法名后缀：获取列表
	 */
	protected static final String SQLNAME_SUFFIX_LIST = "." + SQLNAME_LIST;
	/**
	 * 查询方法名后缀：统计
	 */
	protected static final String SQLNAME_SUFFIX_COUNT = "." + SQLNAME_COUNT;

	private SqlSessionTemplate sqlSessionTemplate;
	
	/**
	 * 对象对应的数据库表名
	 */
	protected Map<Class<?>, String> pojoTableNames = new HashMap<Class<?>, String>();

	/**
	 * 获取实体类的表名
	 * @author lmiky
	 * @date 2014年8月25日 下午10:12:01
	 * @param pojoClass
	 * @return
	 * @throws DatabaseException
	 */
	protected String getPojoTabelName(Class<?> pojoClass) throws DatabaseException {
		// 先读缓存
		String cacheTableName = pojoTableNames.get(pojoClass);
		if (cacheTableName != null) {
			return cacheTableName;
		}
		// 根据反射获取
		Table annotation = pojoClass.getAnnotation(Table.class);
		// 没有对应的注解
		if (annotation == null) {
			throw new DatabaseException(pojoClass.getName() + " is not a db pojo!");
		}
		cacheTableName = annotation.name();
		// 放入缓存
		pojoTableNames.put(pojoClass, cacheTableName);
		return cacheTableName;
	}
	
	/**
	 * 生成参数
	 * @author lmiky
	 * @date 2014年9月9日 上午10:23:01
	 * @param pojoClass
	 * @return
	 */
	protected <T extends BasePojo> Map<String, Object> generateParameterMap(Class<T> pojoClass) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", getPojoTabelName(pojoClass));
		params.put("pojoName", pojoClass.getSimpleName());
		return params;
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#find(java.lang.Class, java.lang.Long)
	 */
	@Override
	public <T extends BasePojo> T find(Class<T> pojoClass, Long id) throws DatabaseException {
		// SqlBuilder.BEGIN();
		// SqlBuilder.SELECT("*");
		// SqlBuilder.FROM(getPojoTabelName(pojoClass));
		// SqlBuilder.WHERE(BasePojo.POJO_FIELD_NAME_ID + " = #{" + BasePojo.POJO_FIELD_NAME_ID + "}");
		// Map<String, Object> values = new HashMap<String, Object>();
		// values.put("sql", SqlBuilder.SQL());
		// values.put("id", id);
		// return sqlSessionTemplate.selectOne("common.executeSql", values);
		PropertyFilter propertyFilter = new PropertyFilter();
		propertyFilter.setCompareClass(pojoClass);
		propertyFilter.setCompareType(PropertyCompareType.EQ);
		propertyFilter.setPropertyName(BasePojo.POJO_FIELD_NAME_ID + "");
		propertyFilter.setPropertyValue(id);
		return find(pojoClass, propertyFilter);
//		return sqlSessionTemplate.selectOne(pojoClass.getName() + SQLNAME_SUFFIX_FIND, id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#find(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	public <T extends BasePojo> T find(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		try {
			PropertyFilter propertyFilter = new PropertyFilter();
			propertyFilter.setCompareClass(pojoClass);
			propertyFilter.setCompareType(PropertyCompareType.EQ);
			propertyFilter.setPropertyName(propertyName);
			propertyFilter.setPropertyValue(propertyValue);
			return find(pojoClass, propertyFilter);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#find(java.lang.Class, java.util.List)
	 */
	public <T extends BasePojo> T find(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		try {
			// 单个实例或者null；当返回的实例大于一个的时候的抛出NonUniqueResultException
			//return sqlSessionTemplate.selectOne("common.executeSelectSql", generateQuery(pojoClass, propertyFilters, null));
			Map<String, Object> params = generateParameterMap(pojoClass);
			params.put(PARAM_NAME_FILTERS, propertyFilters);
			return sqlSessionTemplate.selectOne(pojoClass.getName() + SQLNAME_SUFFIX_FIND, params);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#find(java.lang.Class, com.lmiky.jdp.database.model.PropertyFilter[])
	 */
	public <T extends BasePojo> T find(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		return find(pojoClass, Arrays.asList(propertyFilters));
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#save(com.lmiky.jdp.database.pojo.BasePojo)
	 */
	@Override
	public <T extends BasePojo> void save(T pojo) throws DatabaseException {
		if(pojo.getId() == null) {
			add(pojo);
		} else {
			update(pojo);
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#save(java.util.List)
	 */
	@Override
	public <T extends BasePojo> void save(List<T> pojos) throws DatabaseException {
		for(T pojo: pojos) {
			save(pojo);
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#add(com.lmiky.jdp.database.pojo.BasePojo)
	 */
	@Override
	public <T extends BasePojo> void add(T pojo) throws DatabaseException {
		sqlSessionTemplate.insert(pojo.getClass().getName() + SQLNAME_SUFFIX_ADD, pojo);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#add(java.util.List)
	 */
	@Override
	public <T extends BasePojo> void add(List<T> pojos) throws DatabaseException {
		for(T pojo: pojos) {
			add(pojo);
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#update(com.lmiky.jdp.database.pojo.BasePojo)
	 */
	@Override
	public <T extends BasePojo> void update(T pojo) throws DatabaseException {
		sqlSessionTemplate.update(pojo.getClass().getName() + SQLNAME_SUFFIX_UPDATE, pojo);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#update(java.util.List)
	 */
	@Override
	public <T extends BasePojo> void update(List<T> pojos) throws DatabaseException {
		for(T pojo: pojos) {
			update(pojo);
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#update(java.lang.Class, java.lang.Long, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Long id, String propertyName, Object propertyValue) throws DatabaseException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(propertyName, propertyValue);
		return update(pojoClass, id, params);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#update(java.lang.Class, java.lang.Long, java.util.Map)
	 */
	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Long id, Map<String, Object> params) throws DatabaseException {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put(BasePojo.POJO_FIELD_NAME_ID, id);
		return update(pojoClass, condition, params);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#update(java.lang.Class, java.util.Map, java.util.Map)
	 */
	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Map<String, Object> condition, Map<String, Object> updateValue) throws DatabaseException {
		Map<String, Object> params = generateParameterMap(pojoClass);
		params.put("condition", condition);
		params.put("updateValue", updateValue);
		return sqlSessionTemplate.update(MAPPER_NAMESPACE_COMMON + ".executeUpdate", params) > 0;
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#update(java.lang.Class, java.lang.String, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, String conditionFieldName, Object conditionFieldValue, String updateFieldName, Object updateFieldValue) throws DatabaseException {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put(conditionFieldName, conditionFieldValue);
		Map<String, Object> updateValue = new HashMap<String, Object>();
		condition.put(updateFieldName, updateFieldValue);
		return update(pojoClass, condition, updateValue);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#delete(com.lmiky.jdp.database.pojo.BasePojo)
	 */
	@Override
	public <T extends BasePojo> void delete(T pojo) throws DatabaseException {
		delete(pojo.getClass(), pojo.getId());
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#delete(java.util.List)
	 */
	@Override
	public <T extends BasePojo> void delete(List<T> pojos) throws DatabaseException {
		if(pojos.isEmpty()) {
			return;
		}
		Long[] ids = new Long[pojos.size()];
		for(int i=0; i<pojos.size(); i++) {
			ids[i] = pojos.get(i).getId();
		}
		delete(pojos.get(0).getClass(), ids);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#delete(java.lang.Class, java.lang.Long)
	 */
	@Override
	public <T extends BasePojo> void delete(Class<T> pojoClass, Long id) throws DatabaseException {
//		SqlBuilder.BEGIN();
//		SqlBuilder.DELETE_FROM(getPojoTabelName(pojoClass));
//		SqlBuilder.WHERE(BasePojo.POJO_FIELD_NAME_ID + "=#{id}");
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("sql", SqlBuilder.SQL());
//		params.put("id", id);
//		sqlSessionTemplate.delete("common.executeDelete", params);
		PropertyFilter propertyFilter = new PropertyFilter();
		propertyFilter.setCompareClass(pojoClass);
		propertyFilter.setCompareType(PropertyCompareType.EQ);
		propertyFilter.setPropertyName(BasePojo.POJO_FIELD_NAME_ID + "");
		propertyFilter.setPropertyValue(id);
		delete(pojoClass, propertyFilter);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#delete(java.lang.Class, java.lang.Long[])
	 */
	@Override
	public <T extends BasePojo> void delete(Class<T> pojoClass, Long[] ids) throws DatabaseException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", getPojoTabelName(pojoClass));
		params.put("ids", ids);
		sqlSessionTemplate.delete(MAPPER_NAMESPACE_COMMON + ".executeBatchDeleteByIds", params);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#delete(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		PropertyFilter propertyFilter = new PropertyFilter();
		propertyFilter.setCompareClass(pojoClass);
		propertyFilter.setCompareType(PropertyCompareType.EQ);
		propertyFilter.setPropertyName(propertyName);
		propertyFilter.setPropertyValue(propertyValue);
		return delete(pojoClass, propertyFilter);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#delete(java.lang.Class, java.util.List)
	 */
	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
//		SqlBuilder.BEGIN();
//		SqlBuilder.DELETE_FROM(getPojoTabelName(pojoClass));
//		StringBuffer whereCondition = new StringBuffer(" 1=1 ");
//		Map<String, Object> params = new HashMap<String, Object>();
//		for (PropertyFilter propertyFilter : propertyFilters) {
//			String propertyName = propertyFilter.getPropertyName();
//			String compareClassSimpleName = propertyFilter.getCompareClass().getSimpleName();
//			if (propertyFilter.isCollectionField()) {
//				propertyName = propertyName.substring(propertyName.indexOf(".") + 1);
//			}
//			whereCondition.append(" and ").append(generateCondition(propertyFilter));
//			params.put(compareClassSimpleName + "_" + propertyName, propertyFilter.getPropertyValue());
//		}
//		SqlBuilder.WHERE(whereCondition.toString());
//		params.put("sql", SqlBuilder.SQL());
//		return sqlSessionTemplate.delete("common.executeDelete", params);
		Map<String, Object> params = generateParameterMap(pojoClass);
		params.put(PARAM_NAME_FILTERS, propertyFilters);
		return sqlSessionTemplate.delete(MAPPER_NAMESPACE_COMMON + ".executeDelete", params);
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#delete(java.lang.Class, com.lmiky.jdp.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		return delete(pojoClass, Arrays.asList(propertyFilters));
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#list(java.lang.Class)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass) throws DatabaseException {
		return list(pojoClass, 0, Integer.MAX_VALUE);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#list(java.lang.Class, com.lmiky.jdp.database.model.PropertyFilter, com.lmiky.jdp.database.model.Sort)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, PropertyFilter propertyFilter, Sort sort) throws DatabaseException {
		List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
		if(propertyFilter != null) {
			propertyFilters.add(propertyFilter);	
		}
		List<Sort> sorts = new ArrayList<Sort>();
		if(sort != null) {
			sorts.add(sort);	
		}
		return list(pojoClass, propertyFilters, sorts);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#list(java.lang.Class, java.util.List, java.util.List)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts) throws DatabaseException {
		return list(pojoClass, propertyFilters, sorts, 0, Integer.MAX_VALUE);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#list(java.lang.Class, com.lmiky.jdp.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		return list(pojoClass, 0, Integer.MAX_VALUE, propertyFilters);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#list(java.lang.Class, com.lmiky.jdp.database.model.Sort[])
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, Sort... sorts) throws DatabaseException {
		return list(pojoClass, 0, Integer.MAX_VALUE, sorts);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#list(java.lang.Class, int, int)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize) throws DatabaseException {
		return list(pojoClass, null, null, pageFirst, pageSize);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#list(java.lang.Class, java.util.List, java.util.List, int, int)
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts, int pageFirst, int pageSize) throws DatabaseException {
		Map<String, Object> params = generateParameterMap(pojoClass);
		params.put(PARAM_NAME_FILTERS, propertyFilters);
		params.put(PARAM_NAME_SORTS, sorts);
		params.put(PARAM_NAME_PAGE_FIRST, pageFirst);
		params.put(PARAM_NAME_PAGE_SIZE, pageSize);
		return sqlSessionTemplate.selectList(pojoClass.getName() + SQLNAME_SUFFIX_LIST, params);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#list(java.lang.Class, int, int, com.lmiky.jdp.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize, PropertyFilter... propertyFilters) throws DatabaseException {
		return list(pojoClass, Arrays.asList(propertyFilters), null, pageFirst, pageSize);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#list(java.lang.Class, int, int, com.lmiky.jdp.database.model.Sort[])
	 */
	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize, Sort... sorts) throws DatabaseException {
		return list(pojoClass, null, Arrays.asList(sorts), pageFirst, pageSize);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#count(java.lang.Class)
	 */
	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass) throws DatabaseException {
		return count(pojoClass, new ArrayList<PropertyFilter>());
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#count(java.lang.Class, java.util.List)
	 */
	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		Map<String, Object> params = generateParameterMap(pojoClass);
		if(propertyFilters != null) {
			params.put(PARAM_NAME_FILTERS, propertyFilters);
		}
		return sqlSessionTemplate.selectOne(MAPPER_NAMESPACE_COMMON + SQLNAME_SUFFIX_COUNT, params);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#count(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		PropertyFilter propertyFilter = new PropertyFilter();
		propertyFilter.setCompareClass(pojoClass);
		propertyFilter.setCompareType(PropertyCompareType.EQ);
		propertyFilter.setPropertyName(propertyName);
		propertyFilter.setPropertyValue(propertyValue);
		return count(pojoClass, propertyFilter);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#count(java.lang.Class, com.lmiky.jdp.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		return count(pojoClass, Arrays.asList(propertyFilters));
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#exist(java.lang.Class, java.util.List)
	 */
	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		return count(pojoClass, propertyFilters) > 0;
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#exist(java.lang.Class, com.lmiky.jdp.database.model.PropertyFilter[])
	 */
	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		return exist(pojoClass, Arrays.asList(propertyFilters));
	}

	/* (non-Javadoc)
	 * @see com.lmiky.jdp.database.dao.BaseDAO#exist(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		PropertyFilter propertyFilter = new PropertyFilter();
		propertyFilter.setCompareClass(pojoClass);
		propertyFilter.setCompareType(PropertyCompareType.EQ);
		propertyFilter.setPropertyName(propertyName);
		propertyFilter.setPropertyValue(propertyValue);
		return exist(pojoClass, propertyFilter);
	}

	/**
	 * @return the sqlSessionTemplate
	 */
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	/**
	 * @param sqlSessionTemplate the sqlSessionTemplate to set
	 */
	@Resource(name = "sqlSessionTemplate")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
