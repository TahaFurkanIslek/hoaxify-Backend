package com.hoaxify.ws.user;


import java.util.Collection;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;


import lombok.Data;

@Data
@Entity
@Table(name="users")
public class User implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8421768845853099274L;

	@Id
	@GeneratedValue
	private long id;
	
	@NotNull(message="{hoaxify.constraints.username.NotNull.message}")
	@Size(min = 4,max = 255)
	@UniqueUsername
	private String username;
	
	@NotNull
	@Size(min = 4,max = 255)
	private String displayName;
	
	@NotNull
	@Size(min = 8,max = 255)
	@Pattern(regexp ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",message="{hoaxify.constraints.password.Pattern.message}") 
	private String password;
	
	private String image;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("Role_user");
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
}
