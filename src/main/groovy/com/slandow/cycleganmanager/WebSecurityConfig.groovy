package com.slandow.cycleganmanager

import com.slandow.cycleganmanager.security.JWTAuthenticationFilter
import com.slandow.cycleganmanager.security.JWTAuthorizationFilter
import com.slandow.cycleganmanager.security.SecurityConstants
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity

class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsService userDetailsService

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .cors().and().csrf().disable() // TODO enable these
            .authorizeRequests()
            .antMatchers(*SecurityConstants.UNSECURED_URLS).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            // this disables session creation on Spring Security
            .and()

            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues())
        return source
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        new BCryptPasswordEncoder()
    }
}
