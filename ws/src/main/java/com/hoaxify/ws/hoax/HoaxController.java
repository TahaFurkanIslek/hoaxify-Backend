package com.hoaxify.ws.hoax;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.hoax.vm.HoaxVM;
import com.hoaxify.ws.shared.CurrentUser;
import com.hoaxify.ws.shared.GenericResponse;
import com.hoaxify.ws.user.User;

@RestController
@RequestMapping("/api/1.0")
public class HoaxController {
	@Autowired
	HoaxService hoaxService;
	
	@PostMapping("/hoaxes")
	GenericResponse saveHoax(@Valid @RequestBody Hoax hoax,@CurrentUser User user) {
		hoaxService.save(hoax,user);
		return new GenericResponse("Hoax is saved");
	}
	
	@GetMapping("/hoaxes")
	Page<HoaxVM> getHoaxes(@PageableDefault(sort = "id",direction=Direction.DESC) Pageable page){
		return hoaxService.getHoaxes(page).map(HoaxVM::new);
	} 
	
	@GetMapping({"/hoaxes/{id:[0-9]+}","/users/{username}/hoaxes/{id:[0-9]+}"})
	ResponseEntity<?> getHoaxesRelative(@PageableDefault(sort = "id",direction=Direction.DESC) Pageable page,
			@PathVariable long id,
			@PathVariable(required = false) String username,
			@RequestParam(name = "count",required = false,defaultValue = "false") boolean count,
			@RequestParam(name="direction",defaultValue = "before") String direction){
		if(count) {
			long newHoaxCount=hoaxService.getNewHoaxesCount(id,username);
			Map<String,Long> response=new HashMap<>();
			response.put("count", newHoaxCount);
			return ResponseEntity.ok(response);
		}
		if(direction.equals("after")) {
			List<Hoax> newHoaxes= hoaxService.getNewHoaxes(id,username,page.getSort());
			List<HoaxVM> newHoaxesVM=newHoaxes.stream().map(HoaxVM::new).collect(Collectors.toList());
			return ResponseEntity.ok(newHoaxesVM);
		}
		return ResponseEntity.ok(hoaxService.getOldHoaxes(id,username,page).map(HoaxVM::new));
	} 
	
	@GetMapping("/users/{username}/hoaxes")
	Page<HoaxVM> getUserHoaxes (@PageableDefault(sort="id",direction=Direction.DESC) Pageable page,@PathVariable String username) {
		return hoaxService.getUserHoaxes(page,username).map(HoaxVM::new);
	}
	


}
