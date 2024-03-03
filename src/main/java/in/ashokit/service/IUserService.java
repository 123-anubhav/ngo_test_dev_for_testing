package in.ashokit.service;

import java.util.Optional;

import in.ashokit.dto.UserDTO;
import in.ashokit.model.User;

public interface IUserService {

	public Integer saveUser(User user);

	public Optional<User> findByUserEmail(String userEmail);
}
