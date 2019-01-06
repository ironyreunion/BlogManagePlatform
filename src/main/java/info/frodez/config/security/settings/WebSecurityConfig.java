package info.frodez.config.security.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import info.frodez.config.security.impl.error.AccessDeniedProcessor;
import info.frodez.config.security.impl.error.NoAuthEntryPoint;
import info.frodez.config.security.impl.filter.JwtTokenFilter;
import info.frodez.config.security.settings.SecurityProperties.Cors;

/**
 * spring security配置
 * @author Frodez
 * @date 2018-12-02
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 基础路径
	 */
	@Value("${server.servlet.context-path}/**")
	private String basePath;

	/**
	 * 无验证访问控制
	 */
	@Autowired
	private NoAuthEntryPoint noAuthPoint;

	/**
	 * 无权限访问控制
	 */
	@Autowired
	private AccessDeniedProcessor noAuthProcessor;

	/**
	 * 访问控制参数配置
	 */
	@Autowired
	private SecurityProperties properties;

	/**
	 * 验证信息获取服务
	 */
	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * jwt验证过滤器
	 */
	@Autowired
	private JwtTokenFilter filter;

	/**
	 * 主配置
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling()
			// 无权限时导向noAuthPoint
			.authenticationEntryPoint(noAuthPoint).and().exceptionHandling()
			.accessDeniedHandler(noAuthProcessor).and().sessionManagement()
			// 不创建session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
			// 登录入口不控制
			.antMatchers(properties.getAuth().getPermitAllPath()).permitAll()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll().antMatchers("/swagger-resources/**")
			.permitAll().antMatchers("/swagger-ui.html**").permitAll().antMatchers("/webjars/**")
			.permitAll().anyRequest().authenticated().and()
			// 在密码验证过滤器前执行jwt过滤器
			.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).headers()
			.cacheControl(); // disable page caching
	}

	/**
	 * 静态资源忽略配置
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/swagger-resources/**", "/swagger-ui.html**", "/webjars/**");
	}

	/**
	 * 验证信息获取服务配置
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	/**
	 * 密码加密器
	 * @author Frodez
	 * @date 2018-12-04
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 验证管理器
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * 跨域资源配置
	 * @author Frodez
	 * @date 2018-12-04
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		Cors cors = properties.getCors();
		configuration.setAllowedOrigins(cors.getAllowedOrigins());
		configuration.setAllowedMethods(cors.getAllowedMethods());
		configuration.setAllowedHeaders(cors.getAllowedHeaders());
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
