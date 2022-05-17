 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package planetseed.proyectofinal.configuraciones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import planetseed.proyectofinal.servicios.UsuarioServicio;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfiguracionesSeguridad extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio)
            .passwordEncoder(new BCryptPasswordEncoder()); 
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()
                .and().formLogin()                                                          
                        .loginPage("/login") 
                        .loginProcessingUrl("/logincheck")
                        
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/contenido?").permitAll() 
                .and().logout() 
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout").permitAll()
                .and().csrf().disable();
    }
}
