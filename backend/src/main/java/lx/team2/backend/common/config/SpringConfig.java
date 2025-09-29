package lx.team2.backend.common.config;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@MapperScan(basePackages = "lx.team2.backend.feature")
public class SpringConfig {

    @Autowired
    private org.springframework.core.env.Environment env;

    /*
    @Bean
    DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }
    */

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, ApplicationContext context) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();

        factory.setDataSource(dataSource); // 파라미터로 받은 dataSource를 사용하는 것이 더 좋습니다.

        // mapperLocations 경로를 properties 파일에서 읽어오도록 수정
        factory.setMapperLocations(context.getResources(env.getProperty("mybatis.mapper-locations")));

        // properties 파일의 type-aliases-package 값을 설정에 추가
        factory.setTypeAliasesPackage(env.getProperty("mybatis.type-aliases-package"));

        // 나머지 추가 설정 (mapUnderscoreToCamelCase 등)
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(Boolean.parseBoolean(env.getProperty("mybatis.map-underscore-to-camel-case")));

        factory.setConfiguration(configuration);
        return factory;
    }

    @Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {

        // 1. <constructor-arg index="0" ref="sqlSessionFactory"/>
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }

}