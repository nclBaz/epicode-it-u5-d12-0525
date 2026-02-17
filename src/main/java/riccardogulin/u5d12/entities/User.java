package riccardogulin.u5d12.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User implements UserDetails {
	@Id
	@GeneratedValue
	@Setter(AccessLevel.NONE)
	private UUID id;

	private String name;
	private String surname;
	private String email;
	private String password;
	private String avatarURL;
	@Enumerated(EnumType.STRING)
	private Role role;

	public User(String name, String surname, String email, String password) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.role = Role.USER; // Di default chi si registra su quest'applicazione sarà un utente semplice
		// Eventualmente si può considerare di fare un endpoint per ADMIN/SUPERADMIN per modificare il ruolo di un utente
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Questo metodo deve restituire una collection di Authorities, cioè di RUOLI
		// SimpleGrantedAuthority è una classe che ci permette di creare degli oggetti "ruolo" compatibili con questa collection
		// Quindi passiamo il valore dell'enum a quel costruttore
		return List.of(new SimpleGrantedAuthority(this.role.name()));
		// return List.of(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getUsername() {
		return this.email;
	}
}
