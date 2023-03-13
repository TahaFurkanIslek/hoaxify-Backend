package com.hoaxify.ws.user;


import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.user.vm.UserUpdateVM;
import com.hoaxify.ws.user.vm.UserVM;

@RestController
@RequestMapping("/api/1.0")
public class UserController {
	
	
	@Autowired
	UserService userService;

	@PostMapping("/users")
	public GenericResponse createUser(@Valid @RequestBody User user) {
		userService.save(user);
		return new GenericResponse("user created") ;
	}
	@GetMapping("/users")
	Page<UserVM> getUsers(Pageable page,@CurrentUser User user){ 
		return userService.getUsers(page,user).map(UserVM::new);
	}		
	
	@GetMapping("/users/{username}")
	UserVM getUser(@PathVariable String username) {
		User user=userService.getByUsername(username);
		UserVM userVM=new UserVM(user);
		return userVM;
	}
	@PutMapping("/users/{username}")
	@PreAuthorize("#username ==principal.username")
	UserVM updateUser(@Valid @RequestBody UserUpdateVM userUpdateVM,@PathVariable String username) {
		User user=userService.updateUser(username,userUpdateVM);
		UserVM userVM=new UserVM(user);
		return userVM;
	}
}
