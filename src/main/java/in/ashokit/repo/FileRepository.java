package in.ashokit.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.dto.FileEntityDTO;
import in.ashokit.model.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

	

}
