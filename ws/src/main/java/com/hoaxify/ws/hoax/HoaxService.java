package com.hoaxify.ws.hoax;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.error.NotFoundException;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;

@Service
public class HoaxService {
	
	HoaxRepository hoaxRepository;
	
	UserRepository userRepository;
	

	public HoaxService(HoaxRepository hoaxRepository,UserRepository userRepository) {
		super();
		this.hoaxRepository = hoaxRepository;
		this.userRepository=userRepository;
	}

	public void save(Hoax hoax,User user) {
		hoax.setTimestamp(new Date());
		hoax.setUserP(user);
		hoaxRepository.save(hoax);		
	}

	public Page<Hoax> getHoaxes(Pageable page) {
		return hoaxRepository.findAll(page);		
	}

	public Page<Hoax> getUserHoaxes(Pageable page, String username) {
		User inDB= userRepository.findByUsername(username);
		if(inDB==null) {
			throw new NotFoundException();
		}
		return hoaxRepository.findByUserPUsername(page, username);
	}

	public Page<Hoax> getOldHoaxes(long id, String username, Pageable page) {
		Specification<Hoax> specification=idLessThan(id);
		if(username!=null) {
			User inDB= userRepository.findByUsername(username);
			if(inDB==null) {
				throw new NotFoundException();
			}
			specification=specification.and(userIs(inDB));
		}
		return hoaxRepository.findAll(specification,page);
		
	}

	public long getNewHoaxesCount(long id, String username) {
		Specification<Hoax> specification=idGreaterThan(id);
		if(username!=null) {
			User inDB= userRepository.findByUsername(username);
			if(inDB==null) {
				throw new NotFoundException();
			}
			specification=specification.and(userIs(inDB));
		}
		return hoaxRepository.count(specification);
	}


	public List<Hoax> getNewHoaxes(long id, String username, Sort sort) {
		Specification<Hoax> specification=idGreaterThan(id);
		if(username!=null) {
			User inDB= userRepository.findByUsername(username);
			if(inDB==null) {
				throw new NotFoundException();
			}
			specification=specification.and(userIs(inDB));
		}		
		return hoaxRepository.findAll(specification, sort);	
		
	}

	Specification<Hoax> idLessThan(long id){
		return  (root, query, criteriaBuilder)-> {
				return criteriaBuilder.lessThan(root.get("id"), id);
			};
			
		};	
	Specification<Hoax> userIs(User user){
		return  (root, query, criteriaBuilder)-> {
				return criteriaBuilder.equal(root.get("userP"),user);
			};
				
		};	
	Specification<Hoax> idGreaterThan(long id){
		return  (root, query, criteriaBuilder)-> {
				return criteriaBuilder.greaterThan(root.get("id"), id);
			};
		};			
	
}
