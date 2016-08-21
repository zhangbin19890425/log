package cn.sh.ideal.mybatis.plugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;

/**
 * @ClassName: MyBatis 性能拦截器，用于输出每条 SQL 语句及其执行时间
 * @author: Administrator
 * @date: 2015年11月24日 下午5:05:35
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }) })
public class PerformanceInterceptor implements Interceptor {
	private static final Logger log = Logger.getLogger(PerformanceInterceptor.class);
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("ss.SSS");
	// 3 seconds is slow sql
	protected long slowSqlMillis = 3 * 1000;

	protected boolean logSlowSql = false;

	public void setSlowSqlMillis(long slowSqlMillis) {
		this.slowSqlMillis = slowSqlMillis;
	}

	public void setLogSlowSql(boolean logSlowSql) {
		this.logSlowSql = logSlowSql;
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

		long start = System.currentTimeMillis();
		Object result = invocation.proceed();
		long end = System.currentTimeMillis();
		long millis = end - start;

		if (millis >= slowSqlMillis) {// 查询 执行超时 记录SQL
			log.info("耗时： " + DATE_FORMAT.format(new Date(millis)) + "s . - id:" + mappedStatement.getId());
			if (logSlowSql) {
				String sql = getSql(invocation, mappedStatement);
				log.info("slow sql " + sql + " \n");
			}
		}
		return result;
	}

	private String getSql(Invocation invocation, MappedStatement mappedStatement) {
		Object parameterObject = null;
		if (invocation.getArgs().length > 1) {
			parameterObject = invocation.getArgs()[1];
		}
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		Configuration configuration = mappedStatement.getConfiguration();
		return getSql(boundSql, parameterObject, configuration);
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) { return Plugin.wrap(target, this); }
		return target;
	}

	@Override
	public void setProperties(Properties properties) {}

	private String getSql(BoundSql boundSql, Object parameterObject, Configuration configuration) {
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		if (parameterMappings != null) {
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else {
						MetaObject metaObject = configuration.newMetaObject(parameterObject);
						value = metaObject.getValue(propertyName);
					}
					sql = replacePlaceholder(sql, value);
				}
			}
		}
		return sql;
	}

	private String replacePlaceholder(String sql, Object propertyValue) {
		String result;
		if (propertyValue != null) {
			if (propertyValue instanceof String) {
				result = "'" + propertyValue + "'";
			} else if (propertyValue instanceof Date) {
				result = "'" + DATE_FORMAT.format(propertyValue) + "'";
			} else {
				result = propertyValue.toString();
			}
		} else {
			result = "null";
		}
		return sql.replaceFirst("\\?", result);
	}
}