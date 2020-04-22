package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementacaoUserDetailsServices implementacaoUserDetailsServices;

	@Override //configura as solicitacoes de acesso por Http
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf()
		.disable() //disable as configs padrao de memoria do Spring Security
		.authorizeRequests() //permitir restringir acessos
		.antMatchers(HttpMethod.GET, "/").permitAll() //qualquer user acessa parte inicial do sistema
		.antMatchers(HttpMethod.GET, "/cadastropessoa").hasRole("ADMIN")
		.antMatchers("/materialize/").permitAll()
		.anyRequest().authenticated()
		.and().formLogin().permitAll() //permite qualquer user
		.and().logout() //mapeia url de sair do sistema e invalida user autenticado
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")); //qdo passar URL logout ele invalidade saessao
		
	}
	
	@Override // cria auetnticacao do user com Banco de dfados ou em memoria
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(implementacaoUserDetailsServices)
		.passwordEncoder(new BCryptPasswordEncoder());
		
//		para altenticacao em memoria - usado para teste somente na pratica
//		auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())
//		.withUser("alex")
//		.password("123")
//		.roles("ADMIN");
	
	}
	
	@Override // Ignora URLs especificas
	public void configure(WebSecurity web) throws Exception {
		
		web.ignoring().antMatchers("/materialize/**"); //vai ignorar tudo dentro desta pasta do projeto pois precisa para tela de login
		
	}
	
	
}
