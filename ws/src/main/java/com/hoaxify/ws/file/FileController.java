package com.hoaxify.ws.file;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

	@Autowired
	FileService fileService;
	
	@PostMapping("/api/1.0/hoax-attachments")
	Map<String,String> saveHoaxAttachment(@RequestParam(name="file") MultipartFile multipartFile) {//requestparam kullanmadan değişken ismi olarak clienttan gelen dosyaya uyacak bir isim vererekte çözebilirdim "MultipartFile file" gibi
		String fileName=fileService.saveHoaxAttachment(multipartFile);
		Map<String,String> responseBody=Collections.singletonMap("name",fileName);
		return responseBody;
	}
	
}
