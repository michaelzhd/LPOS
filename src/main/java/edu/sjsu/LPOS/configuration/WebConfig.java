package edu.sjsu.LPOS.configuration;

//import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("edu.sjsu")
public class WebConfig {
//    @Bean
//    public FilterRegistrationBean registerOpenSessionInViewFilterBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        OpenSessionInViewFilter filter = new OpenSessionInViewFilter();
////        OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
//        registrationBean.setFilter(filter);
//        registrationBean.setOrder(5);
//        return registrationBean;
//    }
//    
//    @Bean
//    public HibernateJpaSessionFactoryBean sessionFactory() {
//        return new HibernateJpaSessionFactoryBean();
//    }
//    
//    
}
