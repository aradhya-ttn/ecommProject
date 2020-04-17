package com.bootcamp2020.ecommerceProject.springSecurity;


import com.bootcamp2020.ecommerceProject.filters.CustomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    AppUserDetailsService userDetailsService;

    public ResourceServerConfiguration() {
        super();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new CustomFilter();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }

    @Override
   public void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/register/customer").anonymous()
                .antMatchers("/uploadImage").permitAll()
                .antMatchers("/register/seller").anonymous()
                .antMatchers("/email/sendEmail").anonymous()
                .antMatchers("/registrationConfirm").anonymous()
                .antMatchers("/registrationReconfirm").anonymous()
                .antMatchers("/address/user/{id}").anonymous()
                .antMatchers("/password/reset").anonymous()
                .antMatchers("/password/update").anonymous()
                .antMatchers("/customer/profile").hasAnyRole("CUSTOMER")
                .antMatchers("/customer/address").hasAnyRole("CUSTOMER")
                .antMatchers("/customer/updateProfile").hasAnyRole("CUSTOMER")
                .antMatchers("/customer/updatePassword").hasAnyRole("CUSTOMER")
                .antMatchers("/customer/addAddress").hasAnyRole("CUSTOMER")
                .antMatchers("/customer/deleteAddress").hasAnyRole("CUSTOMER")
                .antMatchers("/customer/updateAddress").hasAnyRole("CUSTOMER")
                .antMatchers("/seller/profile").hasAnyRole("SELLER")
                .antMatchers("/seller/updateProfile").hasAnyRole("SELLER")
                .antMatchers("/seller/updatePassword").hasAnyRole("SELLER")
                .antMatchers("/seller/updateAddress").hasAnyRole("SELLER")
                .antMatchers("/admin/home").hasAnyRole("ADMIN")
                .antMatchers("/admin/customers").hasAnyRole("ADMIN")
                .antMatchers("/admin/sellers").hasAnyRole("ADMIN")
                .antMatchers("/admin/activateCustomer").hasAnyRole("ADMIN")
                .antMatchers("/admin/deactivateCustomer").hasAnyRole("ADMIN")
                .antMatchers("/admin/activateSeller").hasAnyRole("ADMIN")
                .antMatchers("/admin/deactivateSeller").hasAnyRole("ADMIN")
                .antMatchers("/admin/addMetadataField").hasAnyRole("ADMIN")
                .antMatchers("/admin/viewMetadataField").hasAnyRole("ADMIN")
                .antMatchers("/admin/addCategory").hasAnyRole("ADMIN")
                .antMatchers("/admin/viewCategory").hasAnyRole("ADMIN")
                .antMatchers("/admin/viewAllCategory").hasAnyRole("ADMIN")
                .antMatchers("/admin/deleteCategory").hasAnyRole("ADMIN")
                .antMatchers("/admin/updateCategory").hasAnyRole("ADMIN")
                .antMatchers("/admin/addCategoryMetadataFieldValue").hasAnyRole("ADMIN")
                .antMatchers("/admin/updateCategoryMetadataFieldValue").hasAnyRole("ADMIN")
                .antMatchers("/user/home").hasAnyRole("USER")
                .antMatchers("/doLogout").hasAnyRole("ADMIN","USER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}