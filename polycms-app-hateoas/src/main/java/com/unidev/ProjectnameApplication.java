/**
 * Copyright (c) 2016 Denis O <denis@universal-development.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unidev;

import ch.qos.logback.classic.ViewStatusMessagesServlet;
import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polyembeddedcms.PolyCore;
import org.jminix.console.servlet.MiniConsoleServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;

@SpringBootApplication
@ComponentScan("com.unidev")
@EnableCaching
@EnableWebSecurity
public class ProjectnameApplication extends WebSecurityConfigurerAdapter implements ServletContextInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ProjectnameApplication.class, args);
	}

	// -----------------------------------------------------------------------------------------------

	public static final String VERSION = "0.2.0";

	@Value("${admin.user}")
	private String adminUser;

	@Value("${admin.password}")
	private String adminPassword;

	@Value("${storage.root}")
	private String storageRoot;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http
				.authorizeRequests()
				.antMatchers("/jmx/**", "/logs").hasRole("ADMIN")
				.antMatchers("/**", "/").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.logout()
				.permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.inMemoryAuthentication()
				.withUser(adminUser).password(adminPassword).roles("ADMIN");
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addServlet("ViewStatusMessages", ViewStatusMessagesServlet.class).addMapping("/logs");
		servletContext.addServlet("JmxMiniConsoleServlet", MiniConsoleServlet.class).addMapping("/jmx/*");
	}

	@Bean
	public PolyCore polyCore() {
		PolyCore polyCore = new PolyCore();
		polyCore.setStorageRoot(new File(storageRoot));
		return polyCore;
	}

	@Bean
	public WebUtils webUtils() {
		return new WebUtils();
	}

}
