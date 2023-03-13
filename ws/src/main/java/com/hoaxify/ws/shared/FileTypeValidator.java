package com.hoaxify.ws.shared;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hoaxify.ws.file.FileService;

public class FileTypeValidator implements ConstraintValidator<FileType,String>{

	@Autowired
	FileService fileService;
	
	String[] types;
	
	@Override
	public void initialize(FileType constraintAnnotation) {
		this.types=constraintAnnotation.types();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null||value.isEmpty()) {
			return true;
		}
		String fileType=fileService.detectType(value);
		for(String supportedType:this.types) {
			if(fileType.contains(supportedType)){
				return true;
			}
		}
		
		String supportedTypes=Arrays.stream(this.types).collect(Collectors.joining(", "));//arraydeki bütün itemları "," kullanarak birleştiriyor
		
		context.disableDefaultConstraintViolation();//default oluşturulan mesajı engelliyoruz.
		HibernateConstraintValidatorContext hibernateConstraintValidatorContext = context.unwrap(HibernateConstraintValidatorContext.class);
		hibernateConstraintValidatorContext.addMessageParameter("types", supportedTypes);//key value ilişkisi types gördüğü zaman value değerini gönder demek.
		hibernateConstraintValidatorContext.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addConstraintViolation();
		
		return false;
	}

}
