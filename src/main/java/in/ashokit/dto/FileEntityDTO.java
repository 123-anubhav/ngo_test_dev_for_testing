package in.ashokit.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class FileEntityDTO {

	
	    private Long id;

	    private String fileName;

	    @Lob
	    private byte[] data;
	    
	    //private MultipartFile file;
}
