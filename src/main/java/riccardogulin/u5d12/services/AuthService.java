package riccardogulin.u5d12.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import riccardogulin.u5d12.entities.User;
import riccardogulin.u5d12.exceptions.UnauthorizedException;
import riccardogulin.u5d12.payloads.LoginDTO;
import riccardogulin.u5d12.security.JWTTools;

@Service
public class AuthService {

	private final UsersService usersService;
	private final JWTTools jwtTools;
	private final PasswordEncoder bcrypt;

	@Autowired
	public AuthService(UsersService usersService, JWTTools jwtTools, PasswordEncoder bcrypt) {

		this.usersService = usersService;
		this.jwtTools = jwtTools;
		this.bcrypt = bcrypt;
	}

	public String checkCredentialsAndGenerateToken(LoginDTO body) {
		// 1. Controllo credenziali
		// 1.1 Controllo se esiste un utente con quell'email
		User found = this.usersService.findByEmail(body.email());

		// 1.2 Se esiste controllo se la sua password (quella nel DB) è uguale a quella nel body
		
		if (bcrypt.matches(body.password(), found.getPassword())) {
			// 2. Se credenziali OK
			// 2.1 Genero token
			String accessToken = jwtTools.generateToken(found);

			// 2.2 Ritorno token
			return accessToken;

		} else {
			// 3. Se credenziali non ok --> 401 Unauthorized
			throw new UnauthorizedException("Credenziali errate!");
		}


	}
}
