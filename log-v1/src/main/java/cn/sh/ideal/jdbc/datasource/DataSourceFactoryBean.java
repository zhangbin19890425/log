package cn.sh.ideal.jdbc.datasource;

import javax.sql.DataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import net.sf.log4jdbc.SpyLogDelegator;
import net.sf.log4jdbc.tools.Log4JdbcCustomFormatter;
import net.sf.log4jdbc.tools.LoggingType;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class DataSourceFactoryBean implements FactoryBean<DataSource>, InitializingBean {
	DataSource dataSource;
	SpyLogDelegator spyLogDelegator;
	boolean debug = false;

	@Override
	public DataSource getObject() throws Exception {
		return dataSource;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setLogFormatter(SpyLogDelegator spyLogDelegator) {
		this.spyLogDelegator = spyLogDelegator;
	}

	@Override
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dataSource, "Property 'dataSource' is required");
		if (!debug) {
			return;
		}
		Log4jdbcProxyDataSource proxyDataSource = new Log4jdbcProxyDataSource(dataSource);
		if (spyLogDelegator == null) {
			Log4JdbcCustomFormatter format = new Log4JdbcCustomFormatter();
			format.setLoggingType(LoggingType.MULTI_LINE);
			format.setSqlPrefix("SQL:::");
			spyLogDelegator = format;
		}
		proxyDataSource.setLogFormatter(spyLogDelegator);
		dataSource = proxyDataSource;
	}
}
