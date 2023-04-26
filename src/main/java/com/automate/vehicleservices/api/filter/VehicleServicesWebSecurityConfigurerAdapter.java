package com.automate.vehicleservices.api.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class VehicleServicesWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    public static final String URLS_STARTING_WITH_API = "/api/*";
    public static final String CHILD_OR_SUBCHILD_OF_API_PATH = "/api/**";
    private final VehicleServicesCustomFilter vehicleServicesCustomFilter;

    public VehicleServicesWebSecurityConfigurerAdapter(VehicleServicesCustomFilter vehicleServicesCustomFilter) {
        this.vehicleServicesCustomFilter = vehicleServicesCustomFilter;
    }

    @Bean
    public FilterRegistrationBean myFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(vehicleServicesCustomFilter);
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns(URLS_STARTING_WITH_API);
        return filterRegistrationBean;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable().authorizeRequests().antMatchers(CHILD_OR_SUBCHILD_OF_API_PATH)
                .permitAll();
    }
}
