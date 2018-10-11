package com.lanshan.web.admin.config;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.bstek.dorado.hibernate.UnByteCodeProxyInterceptor;
import com.lanshan.core.util.SpringUtils;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration implements EnvironmentAware{

	//配置sessionFactory
	
	private Environment environment;

	@Bean
	public LocalSessionFactoryBean sessionFactory(
			@Qualifier("dataSource")
	        DataSource dataSource,

	 @Value("${spring.jpa.properties.hibernate.packageScan}")
	 String packageScan,
     @Value("${spring.jpa.properties.hibernate.dialect}")
	 String dialect,
     @Value("${spring.jpa.show-sql}")
	 String showSql,
     @Value("${spring.jpa.properties.hibernate.format_sql}")
	 String formatSql,
     @Value("${spring.jpa.properties.hibernate.use_sql_comments}")
	 String useSqlComments,
     @Value("${spring.jpa.properties.hibernate.ddl-auto}")
	 String ddlAuto) throws SQLException {

	      LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();

	      localSessionFactoryBean.setDataSource(dataSource());

	      localSessionFactoryBean.setPackagesToScan(packageScan);
	      
	      
	      
	      localSessionFactoryBean.setEntityInterceptor(unByteCodeProxyInterceptor());

	      Properties properties = new Properties();

	      properties.setProperty("hibernate.dialect", dialect);

	      properties.setProperty("hibernate.show_sql", showSql);

	      properties.setProperty("hibernate.format_sql", formatSql);

	      properties.setProperty("hibernate.use_sql_comments", useSqlComments);

	      properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);

	      localSessionFactoryBean.setHibernateProperties(properties);

	      return localSessionFactoryBean;

	  }


	  //配置hibernate事务处理

	@Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {

	      HibernateTransactionManager transactionManager = new HibernateTransactionManager();

	      transactionManager.setSessionFactory(sessionFactory);

	      return transactionManager;

	  }
	
	@Bean 
	public Interceptor unByteCodeProxyInterceptor(){

		return new UnByteCodeProxyInterceptor();
	}


	@Override
	public void setEnvironment(Environment environment) {
		// TODO Auto-generated method stub
		this.environment = environment;
		
	}
	
	@Bean(initMethod = "init", destroyMethod = "close")
    public DataSource dataSource() throws SQLException {
        if (StringUtils.isEmpty(environment.getProperty("spring.datasource.url"))) {
            System.out.println("Your database connection pool configuration is incorrect!" +
                    " Please check your Spring profile, current profiles are:"+
            Arrays.toString(environment.getActiveProfiles()));
            throw new ApplicationContextException(
                    "Database connection pool is not configured correctly");
        }
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(environment.getProperty("spring.datasource.url"));
        druidDataSource.setUsername(environment.getProperty("spring.datasource.username"));
        druidDataSource.setPassword(environment.getProperty("spring.datasource.password"));
        druidDataSource.setInitialSize(1);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(20);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("SELECT　'x'");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        return druidDataSource;
    }
    

}
