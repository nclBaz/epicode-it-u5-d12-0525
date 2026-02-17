package riccardogulin.u5d12.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import riccardogulin.u5d12.entities.User;
import riccardogulin.u5d12.exceptions.ValidationException;
import riccardogulin.u5d12.payloads.LoginDTO;
import riccardogulin.u5d12.payloads.LoginResponseDTO;
import riccardogulin.u5d12.payloads.UserDTO;
import riccardogulin.u5d12.services.AuthService;
import riccardogulin.u5d12.services.UsersService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	private final UsersService usersService;

	public AuthController(AuthService authService, UsersService usersService) {
		this.authService = authService;
		this.usersService = usersService;
	}

	@PostMapping("/login")
	public LoginResponseDTO login(@RequestBody LoginDTO body) {

		return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@RequestBody @Validated UserDTO payload, BindingResult validationResult) {
		// @Validated serve per attivare la validazione, se non lo usiamo è come non farla

		if (validationResult.hasErrors()) {

//			String errors = validationResult.getFieldErrors().stream()
//					.map(fieldError -> fieldError.getDefaultMessage())
//					.collect(Collectors.joining(". "));
//
//			throw new ValidationException(errors);
			List<String> errorsList = validationResult.getFieldErrors()
					.stream()
					.map(fieldError -> fieldError.getDefaultMessage())
					.toList();

			throw new ValidationException(errorsList);
		} else {
			return this.usersService.save(payload);
		}

	}
}
