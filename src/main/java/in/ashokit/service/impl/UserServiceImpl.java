package in.ashokit.service.impl;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.ashokit.model.User;
import in.ashokit.repo.UserRepository;
import in.ashokit.service.IUserService;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService{

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public Integer saveUser(User user) {
		String pwd = encoder.encode( user.getPassword());
		user.setPassword(pwd);
		
		user = repo.save(user);
		return user.getUserId();
	}

	public Optional<User> findByUserEmail(String emailId) {
		return repo.findByEmailId(emailId);
	}
	
	@Override
	public UserDetails loadUserByUsername(String emailId)
			throws UsernameNotFoundException {

		Optional<User> opt = findByUserEmail(emailId);
		//opt.isPresent() -> use insted of isEmply
//		if(opt.isEmpty()) {
//			throw new UsernameNotFoundException(emailId + " not exist");
//		} 
		
		//Entity clas user object
		User user = opt.get();
		
		//Spring Security user object (un,pwd, Collection<GA>)
		return new org.springframework.security.core.userdetails.User(
				user.getEmailId(), 
				user.getPassword(), 
				user.getUserRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toSet())
				);
	}

}
