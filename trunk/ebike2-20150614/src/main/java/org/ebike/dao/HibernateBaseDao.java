package org.ebike.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @ClassName: HibernateBaseDao 
 * @Description: 基于Hibernate的DAO类
 * @author hzjintianfan
 * @date 2014-7-23 下午6:39:04
 */
@Transactional
public class HibernateBaseDao
{

	private SessionFactory sessionFactory;

	public Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 将map中值为null的统一设成一个不可能取到的String
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void fillNullValue(Map map)
	{
		if (map != null)
		{
			for (Object key : map.keySet())
			{
				if (map.get(key) == null)
				{
					map.put(key, "不能这么搞，不要传null进来!");
				}
			}
		}
	}

	public String add(Object entity)
	{
		if (entity instanceof Collection)
		{
			for (Object element : (Collection) entity)
			{
				add(element);
			}
			return null;
		}
		else if (entity instanceof Object[])
		{
			for (Object element : (Object[]) entity)
			{
				add(element);
			}
			return null;
		}
		return (String) getSession().save(entity);
	}

	public void addOrModify(Object entity)
	{
		if (entity instanceof Collection)
		{
			for (Object element : (Collection) entity)
			{
				addOrModify(element);
			}
		}
		else if (entity instanceof Object[])
		{
			for (Object element : (Object[]) entity)
			{
				addOrModify(element);
			}
		}
		else
		{
			getSession().saveOrUpdate(entity);
		}
	}

	public void delete(Object entity)
	{
		if (entity instanceof Collection)
		{
			for (Object element : (Collection) entity)
			{
				delete(element);
			}
		}
		else if (entity instanceof Object[])
		{
			for (Object element : (Object[]) entity)
			{
				delete(element);
			}
		}
		else
		{
			Object persistentEntity = getSession().merge(entity);
			getSession().delete(persistentEntity);
		}
	}

	public void delete(Class entityClass, Serializable id)
	{
		Object persistentEntity = getSession().get(entityClass, id);
		if (persistentEntity != null)
		{
			getSession().delete(persistentEntity);
		}
	}

	public void modify(Object entity)
	{
		if (entity instanceof Collection)
		{
			for (Object element : (Collection) entity)
			{
				modify(element);
			}
		}
		else if (entity instanceof Object[])
		{
			for (Object element : (Object[]) entity)
			{
				modify(element);
			}
		}
		else
		{
			getSession().merge(entity);
		}
	}

	public <T> T query(Class<T> entityClass, Serializable id)
	{
		return (T) getSession().get(entityClass, id);
	}
	
	public <T> T query(Class<T> entityClass, Serializable id,LockMode lockMode)
	{
		return (T) getSession().get(entityClass, id,lockMode);
	}

	public List query(String queryString)
	{
		return query(queryString, null, null, null);
	}

	public List query(String queryString, Map paraMap)
	{
		return query(queryString, paraMap, null, null);
	}

	public List query(final String queryString, final Object... values)
	{
		Query query = getSession().createQuery(queryString);
		for (int position = 0; position < values.length; position++)
		{
			query.setParameter(position, values[position]);
		}
		return query.list();
	}

	public List query(String queryString, Integer firstResult, Integer maxResults)
	{
		return query(queryString, null, firstResult, maxResults);
	}

	public List query(final String queryString, final Map paraMap, final Integer firstResult, final Integer maxResults)
	{
		Query query = getSession().createQuery(queryString);
		if (paraMap != null)
		{
			query.setProperties(paraMap);
		}
		if (firstResult != null)
		{
			query.setFirstResult(firstResult);
		}
		if (maxResults != null)
		{
			query.setMaxResults(maxResults);
		}
		return query.list();
	}

	public Object queryUnique(String queryString)
	{
		return queryUnique(queryString, Collections.EMPTY_MAP);
	}

	public Object queryUnique(final String queryString, final Map paraMap)
	{
		Query query = getSession().createQuery(queryString);
		if (paraMap != null)
		{
			query.setProperties(paraMap);
		}
		return query.uniqueResult();
	}

	public Object queryUnique(final String queryString, final Object... values)
	{
		Query query = getSession().createQuery(queryString);
		for (int position = 0; position < values.length; position++)
		{
			query.setParameter(position, values[position]);
		}
		return query.uniqueResult();
	}

	public long count(String queryString)
	{
		return count(queryString, Collections.EMPTY_MAP);
	}

	public long count(final String queryString, final Map paraMap)
	{
		List args = new ArrayList();
		Matcher matcher = Pattern.compile(":(\\w+)").matcher(queryString);
		while (matcher.find())
		{
			args.add(paraMap.get(matcher.group(1)));
		}
		return count(queryString.replaceAll(":(\\w+)", "?"), args.toArray());
	}

	public long count(final String queryString, final Object... values)
	{
		String tmp = queryString.toLowerCase().replaceAll("\\s+", " ");
		if (!tmp.contains("group by") && !tmp.contains("distinct"))
		{
			String countQueryString = "select count(*) " + queryString.substring(queryString.indexOf("from "));
			return (Long) queryUnique(countQueryString, values);
		}

		QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(null, queryString, Collections.EMPTY_MAP,
				(SessionFactoryImplementor) sessionFactory);
		queryTranslator.compile(Collections.EMPTY_MAP, false);

		final String countQueryString = "select count(*) as result from (" + queryTranslator.getSQLString() + ")";
		SQLQuery query = getSession().createSQLQuery(countQueryString);
		for (int position = 0; position < values.length; position++)
		{
			query.setParameter(position, values[position]);
		}
		query.addScalar("result", StandardBasicTypes.BIG_DECIMAL);
		return ((Number) query.uniqueResult()).longValue();
	}

	public void execute(String statement)
	{
		execute(statement, Collections.EMPTY_MAP);
	}

	public void execute(final String statement, final Map paraMap)
	{
		Query query = getSession().createQuery(statement);
		if (paraMap != null)
		{
			query.setProperties(paraMap);
		}
		query.executeUpdate();
	}

	public void execute(final String statement, Object... values)
	{
		Query query = getSession().createQuery(statement);
		for (int position = 0; position < values.length; position++)
		{
			query.setParameter(position, values[position]);
		}
		query.executeUpdate();
	}

	// ////////////////////////////以下为IHibernateBaseDao声明的方法////////////////////////////////////

	public void flush()
	{
		getSession().flush();
	}

	public void clear()
	{
		getSession().clear();
	}

	public boolean contains(Object entity)
	{
		return getSession().contains(entity);
	}

	public void evict(Object entity)
	{
		getSession().evict(entity);
	}

	public Object merge(Object entity)
	{
		return getSession().merge(entity);
	}

	public void persist(Object entity)
	{
		getSession().persist(entity);
	}

	public void refresh(Object entity)
	{
		getSession().refresh(entity);
	}

	public Iterator iterate(String queryString)
	{
		Query query = getSession().createQuery(queryString);
		return query.iterate();
	}

	public Iterator iterate(String queryString, Object... values)
	{
		Query query = getSession().createQuery(queryString);
		for (int position = 0; position < values.length; position++)
		{
			query.setParameter(position, values[position]);
		}
		return query.iterate();
	}

	public void deleteAll(Class entityClass, List<Serializable> ids)
	{
		for (Serializable id : ids)
		{
			delete(entityClass, id);
		}
	}

	public <T> Set<T> query(final Class<T> entityClass, Serializable[] ids)
	{
		Set<T> result = new HashSet<T>();
		for (Serializable id : ids)
		{
			result.add(query(entityClass, id));
		}
		return result;
	}

	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

}
