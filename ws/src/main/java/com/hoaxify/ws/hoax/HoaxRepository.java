package com.hoaxify.ws.hoax;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HoaxRepository extends JpaRepository<Hoax, Long>,JpaSpecificationExecutor<Hoax>{
	Page<Hoax> findByUserPUsername(Pageable page,String username);
	
	
	
	
	
	
	
//	Page<Hoax> findByIdLessThan(long id,Pageable page);
	 
//	Page<Hoax> findByIdLessThanAndUserPUsername(long id, String username,Pageable page);
	
//	long countByIdGreaterThan(long id);
//	
//	long countByIdGreaterThanAndUserPUsername(long id,String username);
	
//	List<Hoax> findByIdGreaterThan(long id,Sort sort);
	
//	List<Hoax> findByIdGreaterThanAndUserPUsername(long id,String username,Sort sort);
}
