package riccardogulin.u5d12.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import riccardogulin.u5d12.entities.User;
import riccardogulin.u5d12.payloads.UserPayload;
import riccardogulin.u5d12.services.UsersService;

import java.util.UUID;

/*

1. POST http://localhost:3001/users
2. GET http://localhost:3001/users
3. GET http://localhost:3001/users/{userId}
4. PUT http://localhost:3001/users/{userId}
5. DELETE http://localhost:3001/users/{userId}

*/

@RestController
@RequestMapping("/users")
public class UsersController {
	private final UsersService usersService;

	@Autowired
	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}


	@GetMapping
	@PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')") // Solo admin o superadmin possono visualizzare la lista degli utenti
	public Page<User> findAll(@RequestParam(defaultValue = "0") int page,
	                          @RequestParam(defaultValue = "10") int size,
	                          @RequestParam(defaultValue = "surname") String orderBy,
	                          @RequestParam(defaultValue = "asc") String sortCriteria) {

		return this.usersService.findAll(page, size, orderBy, sortCriteria);
	}

	@GetMapping("/{userId}")
	public User findById(@PathVariable UUID userId) {
		return this.usersService.findById(userId);
	}

	@PutMapping("/me")
	public User updateProfile(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody UserPayload payload) {
		return this.usersService.findByIdAndUpdate(currentAuthenticatedUser.getId(), payload);
	}

	@DeleteMapping("/me")
	public void deleteProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
		this.usersService.findByIdAndDelete(currentAuthenticatedUser.getId());
	}

	@PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
	@PutMapping("/{userId}")
	public User findByIdAndUpdate(@PathVariable UUID userId, @RequestBody UserPayload payload) {
		return this.usersService.findByIdAndUpdate(userId, payload);
	}

	@PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void findByIdAndDelete(@PathVariable UUID userId) {
		this.usersService.findByIdAndDelete(userId);
	}

//	@PatchMapping("/{userId}/avatar")
//	public String uploadImage(@RequestParam("profile_picture") MultipartFile file, @PathVariable UUID userId) {
//		// profile_picture deve corrispondere ESATTAMENTE al campo del Form Data dove viene inserito il file
//		// se così non è il file non verrà trovato
//
//		String url = this.usersService.uploadAvatar(file);
//
//		return url; // TODO: cambiare return da String ad un payload in JSON
//	}
}
