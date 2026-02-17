package riccardogulin.u5d12.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Questa non sarà una classe di configurazione qualsiasi ma sarà una apposita per Spring Security
@EnableMethodSecurity // Annotazione OBBLIGATORIA se vogliamo usare le varie @PreAuthorize sugli endpoint <------------------------------
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		// Questo Bean mi consente di configurare le impostazioni di sicurezza di Spring Security, potrò ad es:

		// - disabilitare comportamenti di default che non voglio
		httpSecurity.formLogin(formLogin -> formLogin.disable()); // Non voglio l'autenticazione via form
		// proposta di default da Spring Security (avremo come frontend React per quello)
		httpSecurity.csrf(csrf -> csrf.disable()); // Non voglio la protezione da attacchi CSRF (non serve nel
		// caso dell'autenticazione tramite JWT, anzi ci complicherebbe la vita anche lato FE)
		httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// L'autenticazione JWT è l'esatto opposto del lavorare tramite sessioni, quindi lavoriamo in modalità STATELESS

		// - personalizzare il comportamento di funzionalità pre-esistenti
		httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());
		// Disabilitiamo il meccanismo di default di Spring Security che ritorna 401 ad ogni richiesta.
		// Siccome andremo ad implementare un meccanismo di autenticazione custom, sarà il nostro metodo di autenticazione e controllo a proteggere
		// i vari endpoint, non Security direttamente

		// - aggiungere ulteriori funzionalità custom


		return httpSecurity.build();

	}

	@Bean
	public PasswordEncoder getBCrypt() {
		return new BCryptPasswordEncoder(12);
	}
}
