package in.ashokit.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.ashokit.model.FileEntity;
import in.ashokit.repo.FileRepository;

@Service
public class FileService {

	@Autowired
	private FileRepository fileRepository;

	public void saveFile(MultipartFile multipartFile) throws IOException {
		System.out.println("FileRepository::FileService.saveFile()" + multipartFile);
		FileEntity file = new FileEntity();
		file.setFileName(multipartFile.getOriginalFilename());
		file.setData(multipartFile.getBytes());
		try {
			System.out.println("try block of saveFile () :: starts");
			FileEntity save = fileRepository.save(file);
			System.out.println("try block of saveFile () :: " + save);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("FileService.saveFile():: saveFile :: file saved sucess " + multipartFile);
	}

	public FileEntity getFile(Long id) {
		return fileRepository.findById(id).orElse(null);

	}
}
