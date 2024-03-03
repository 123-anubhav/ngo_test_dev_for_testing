package in.ashokit.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ashokit.model.PaymentDetails;
import in.ashokit.model.User;

public interface PaymentDetailsRepo extends JpaRepository<PaymentDetails, Integer> {
	
	Set<PaymentDetails> findByMobileNumber(String mobileNumber);
	
	Optional<PaymentDetails> findByEmailId(String emailId);

}
