package com.lmiky.jdp.database.dao.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Table;

import org.apache.ibatis.jdbc.SqlBuilder;
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
	
	//sql方法名
		/**
		 * sql方法名：查询
		 */
		protected static final String SQLNAME_FIND = "find";
		
		//查询方法名后缀
		/**
		 * 查询方法名后缀：查询
		 */
		private static final String SQLNAME_SUFFIX_FIND = "." + SQLNAME_FIND;

	private SqlSessionTemplate sqlSessionTemplate;

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
			return sqlSessionTemplate.selectOne("common.executeSelectSql", generateQuery(pojoClass, propertyFilters, null));
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

	@Override
	public <T extends BasePojo> void save(T pojo) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BasePojo> void save(List<T> pojos) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BasePojo> void add(T pojo) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BasePojo> void add(List<T> pojos) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BasePojo> void update(T pojo) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BasePojo> void update(List<T> pojos) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Long id, String propertyName, Object propertyValue) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Long id, Map<String, Object> params) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, Map<String, Object> condition, Map<String, Object> updateValue) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends BasePojo> boolean update(Class<T> pojoClass, String conditionFieldName, Object conditionFieldValue, String updateFieldName, Object updateFieldValue) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends BasePojo> void delete(T pojo) throws DatabaseException {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends BasePojo> void delete(List<T> pojos) throws DatabaseException {

	}

	@Override
	public <T extends BasePojo> void delete(Class<T> pojoClass, Long id) throws DatabaseException {
		SqlBuilder.BEGIN();
		SqlBuilder.DELETE_FROM(getPojoTabelName(pojoClass));
		SqlBuilder.WHERE(BasePojo.POJO_FIELD_NAME_ID + "=#{id}");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sql", SqlBuilder.SQL());
		params.put("id", id);
		sqlSessionTemplate.delete("common.executeDelete", params);
	}

	@Override
	public <T extends BasePojo> void delete(Class<T> pojoClass, Long[] ids) throws DatabaseException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", getPojoTabelName(pojoClass));
		params.put("ids", ids);
		sqlSessionTemplate.delete("common.executeBatchDeleteByIds", params);
	}

	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		PropertyFilter propertyFilter = new PropertyFilter();
		propertyFilter.setCompareClass(pojoClass);
		propertyFilter.setCompareType(PropertyCompareType.EQ);
		propertyFilter.setPropertyName(propertyName);
		propertyFilter.setPropertyValue(propertyValue);
		return delete(pojoClass, propertyFilter);
	}

	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		SqlBuilder.BEGIN();
		SqlBuilder.DELETE_FROM(getPojoTabelName(pojoClass));
		StringBuffer whereCondition = new StringBuffer(" 1=1 ");
		Map<String, Object> params = new HashMap<String, Object>();
		for (PropertyFilter propertyFilter : propertyFilters) {
			String propertyName = propertyFilter.getPropertyName();
			String compareClassSimpleName = propertyFilter.getCompareClass().getSimpleName();
			if (propertyFilter.isCollectionField()) {
				propertyName = propertyName.substring(propertyName.indexOf(".") + 1);
			}
			whereCondition.append(" and ").append(generateCondition(propertyFilter));
			params.put(compareClassSimpleName + "_" + propertyName, propertyFilter.getPropertyValue());
		}
		SqlBuilder.WHERE(whereCondition.toString());
		params.put("sql", SqlBuilder.SQL());
		return sqlSessionTemplate.delete("common.executeDelete", params);
	}

	public String generateCondition(PropertyFilter filter) {
		String propertyName = filter.getPropertyName();
		String compareClassSimpleName = filter.getCompareClass().getSimpleName();
		if (filter.isCollectionField()) {
			propertyName = propertyName.substring(propertyName.indexOf(".") + 1);
		}
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(propertyName);
		if (filter.getCompareType() == PropertyCompareType.EQ) {
			sqlBuf.append(" = #{" + compareClassSimpleName + "_" + propertyName + "}");
		} else if (filter.getCompareType() == PropertyCompareType.NE) {
			sqlBuf.append(" != #{" + compareClassSimpleName + "_" + propertyName + "}");
		} else if (filter.getCompareType() == PropertyCompareType.GT) {
			sqlBuf.append(" > #{" + compareClassSimpleName + "_" + propertyName + "}");
		} else if (filter.getCompareType() == PropertyCompareType.GE) {
			sqlBuf.append(" >= #{" + compareClassSimpleName + "_" + propertyName + "}");
		} else if (filter.getCompareType() == PropertyCompareType.LT) {
			sqlBuf.append(" < #{" + compareClassSimpleName + "_" + propertyName + "}");
		} else if (filter.getCompareType() == PropertyCompareType.LE) {
			sqlBuf.append(" <= #{" + compareClassSimpleName + "_" + propertyName + "}");
		} else if (filter.getCompareType() == PropertyCompareType.LIKE) {
			sqlBuf.append(" like '%").append(filter.getPropertyValue()).append("%' ");
		} else if (filter.getCompareType() == PropertyCompareType.LLIKE) {
			sqlBuf.append(" like '%").append(filter.getPropertyValue()).append("' ");
		} else if (filter.getCompareType() == PropertyCompareType.RLIKE) {
			sqlBuf.append(" like '").append(filter.getPropertyValue()).append("%' ");
		} else if (filter.getCompareType() == PropertyCompareType.NLIKE) {
			sqlBuf.append(" not like '%").append(filter.getPropertyValue()).append("%' ");
		} else if (filter.getCompareType() == PropertyCompareType.NLLIKE) {
			sqlBuf.append(" not like '%").append(filter.getPropertyValue()).append("' ");
		} else if (filter.getCompareType() == PropertyCompareType.NRLIKE) {
			sqlBuf.append(" not like '").append(filter.getPropertyValue()).append("%' ");
		} else if (filter.getCompareType() == PropertyCompareType.NNULL) {
			sqlBuf.append(" is not null ");
		} else if (filter.getCompareType() == PropertyCompareType.NULL) {
			sqlBuf.append(" is null ");
		}
		return sqlBuf.toString();
	}

	@Override
	public <T extends BasePojo> int delete(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		return delete(pojoClass, Arrays.asList(propertyFilters));
	}

	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, PropertyFilter propertyFilter, Sort sort) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, Sort... sorts) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts, int pageFirst, int pageSize) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize, PropertyFilter... propertyFilters) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BasePojo> List<T> list(Class<T> pojoClass, int pageFirst, int pageSize, Sort... sorts) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass) throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T extends BasePojo> int count(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, List<PropertyFilter> propertyFilters) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, PropertyFilter... propertyFilters) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends BasePojo> boolean exist(Class<T> pojoClass, String propertyName, Object propertyValue) throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 生成查询对象
	 * @author lmiky
	 * @date 2013-4-16
	 * @param pojoClass
	 * @param propertyFilters
	 * @param sorts
	 * @return
	 */
	protected <T extends BasePojo> Map<String, Object> generateQuery(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts) {
		String sql = generateSql(pojoClass, propertyFilters, sorts);
		System.out.println(sql);
		Map<String, Object> query = getFilterValues(propertyFilters);
		query.put("sql", sql);
		return query;
	}

	/**
	 * 生成HQL
	 * @author lmiky
	 * @date 2013-4-16
	 * @param pojoClass
	 * @param propertyFilters 过滤条件
	 * @param sorts 排序
	 * @return
	 */
	protected <T extends BasePojo> String generateSql(Class<T> pojoClass, List<PropertyFilter> propertyFilters, List<Sort> sorts) {
		String pojoSimpleName = pojoClass.getSimpleName();
		SqlBuilder.BEGIN();
		SqlBuilder.SELECT("distinct " + pojoSimpleName + ".*");
		SqlBuilder.FROM(getPojoTabelName(pojoClass) + " " + pojoSimpleName);
		if (propertyFilters != null && !propertyFilters.isEmpty()) {
			List<String> joinClassNames = new ArrayList<String>();
			for (PropertyFilter propertyFilter : propertyFilters) {
				if (propertyFilter.isCollectionField()) {
					String compareClassSimpleName = propertyFilter.getCompareClass().getSimpleName();
					// 已关联
					if (joinClassNames.contains(compareClassSimpleName)) {
						continue;
					}
					String comparePropertyName = propertyFilter.getPropertyName().substring(0, propertyFilter.getPropertyName().indexOf("."));
					SqlBuilder.INNER_JOIN(getPojoTabelName(propertyFilter.getCompareClass()) + " " + compareClassSimpleName + " on " + pojoSimpleName + "." + comparePropertyName + "="
							+ compareClassSimpleName + "." + BasePojo.POJO_FIELD_NAME_ID);
					SqlBuilder.WHERE(compareClassSimpleName + "." + comparePropertyName + "=#{" + compareClassSimpleName + "_" + comparePropertyName + "}");
					joinClassNames.add(compareClassSimpleName);
				}
			}
		}
		return prepareHql(propertyFilters, sorts);
	}

	/**
	 * 设置HQL
	 * @author lmiky
	 * @date 2013-4-16
	 * @param hql
	 * @param propertyFilters 过滤条件
	 * @param sorts 排序
	 * @return
	 */
	protected String prepareHql(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
		if (propertyFilters != null && !propertyFilters.isEmpty()) {
			for (PropertyFilter filter : propertyFilters) {
				String propertyName = filter.getPropertyName();
				String compareClassSimpleName = filter.getCompareClass().getSimpleName();
				if (filter.isCollectionField()) {
					propertyName = propertyName.substring(propertyName.indexOf(".") + 1);
				}
				StringBuffer sqlBuf = new StringBuffer();
				sqlBuf.append(filter.getCompareClass().getSimpleName()).append(".").append(propertyName);
				if (filter.getCompareType() == PropertyCompareType.EQ) {
					sqlBuf.append(" = #{" + compareClassSimpleName + "_" + propertyName + "}");
				} else if (filter.getCompareType() == PropertyCompareType.NE) {
					sqlBuf.append(" != #{" + compareClassSimpleName + "_" + propertyName + "}");
				} else if (filter.getCompareType() == PropertyCompareType.GT) {
					sqlBuf.append(" > #{" + compareClassSimpleName + "_" + propertyName + "}");
				} else if (filter.getCompareType() == PropertyCompareType.GE) {
					sqlBuf.append(" >= #{" + compareClassSimpleName + "_" + propertyName + "}");
				} else if (filter.getCompareType() == PropertyCompareType.LT) {
					sqlBuf.append(" < #{" + compareClassSimpleName + "_" + propertyName + "}");
				} else if (filter.getCompareType() == PropertyCompareType.LE) {
					sqlBuf.append(" <= #{" + compareClassSimpleName + "_" + propertyName + "}");
				} else if (filter.getCompareType() == PropertyCompareType.LIKE) {
					sqlBuf.append(" like '%").append(filter.getPropertyValue()).append("%' ");
				} else if (filter.getCompareType() == PropertyCompareType.LLIKE) {
					sqlBuf.append(" like '%").append(filter.getPropertyValue()).append("' ");
				} else if (filter.getCompareType() == PropertyCompareType.RLIKE) {
					sqlBuf.append(" like '").append(filter.getPropertyValue()).append("%' ");
				} else if (filter.getCompareType() == PropertyCompareType.NLIKE) {
					sqlBuf.append(" not like '%").append(filter.getPropertyValue()).append("%' ");
				} else if (filter.getCompareType() == PropertyCompareType.NLLIKE) {
					sqlBuf.append(" not like '%").append(filter.getPropertyValue()).append("' ");
				} else if (filter.getCompareType() == PropertyCompareType.NRLIKE) {
					sqlBuf.append(" not like '").append(filter.getPropertyValue()).append("%' ");
				} else if (filter.getCompareType() == PropertyCompareType.NNULL) {
					sqlBuf.append(" is not null ");
				} else if (filter.getCompareType() == PropertyCompareType.NULL) {
					sqlBuf.append(" is null ");
				}
				SqlBuilder.WHERE(sqlBuf.toString());
			}
		}

		if (sorts != null && !sorts.isEmpty()) {
			for (Sort sort : sorts) {
				StringBuffer orderBuf = new StringBuffer();
				orderBuf.append(sort.getSortClass().getSimpleName()).append(".").append(sort.getPropertyName()).append(" ").append(sort.getSortType());
				SqlBuilder.ORDER_BY(orderBuf.toString());
			}
		}
		return SqlBuilder.SQL();
	}

	/**
	 * 获取过滤条件值
	 * @author lmiky
	 * @date 2013-4-16
	 * @param propertyFilters
	 * @return
	 */
	protected Map<String, Object> getFilterValues(List<PropertyFilter> propertyFilters) {
		Map<String, Object> values = new HashMap<String, Object>();
		if (propertyFilters != null && !propertyFilters.isEmpty()) {
			for (PropertyFilter filter : propertyFilters) {
				// like或null直接在SQL拼写的时候写入
				if (filter.getPropertyValue() != null && filter.getCompareType() != PropertyCompareType.LIKE && filter.getCompareType() != PropertyCompareType.LLIKE
						&& filter.getCompareType() != PropertyCompareType.RLIKE && filter.getCompareType() != PropertyCompareType.NLIKE && filter.getCompareType() != PropertyCompareType.NLLIKE
						&& filter.getCompareType() != PropertyCompareType.NRLIKE && filter.getCompareType() != PropertyCompareType.NNULL && filter.getCompareType() != PropertyCompareType.NULL) {
					String compareClassSimpleName = filter.getCompareClass().getSimpleName();
					String comparePropertyName = filter.getPropertyName();
					if (filter.isCollectionField()) {
						comparePropertyName = filter.getPropertyName().substring(0, filter.getPropertyName().indexOf("."));
					}
					values.put(compareClassSimpleName + "_" + comparePropertyName, filter.getPropertyValue());
				}
			}
		}
		return values;
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
