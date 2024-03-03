package in.ashokit.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmailId(String emailId);
	
	//Optional<User> findAllByUserName(String userName);

	Optional<User> findByUserName(String userName);

	Optional<User> findAllByUserName(String userName);
	
	//Optional<User> findByMobileNumber(String mobileNumber);
	
	//Optional<User> findByUserMobileNumber(String userMobileNumber);
}
