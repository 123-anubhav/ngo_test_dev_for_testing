package in.ashokit.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
//@Table(name="ngo_payment_details")
public class PaymentDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transferId;

	private String name;
	private String mobileNumber;
	
	private String emailId; // internally this is mobile number

	private String payeeAccountNumber;
	private String payerAccountNumber;
	private Double amount;
	private String payeeUpiId;
	private String payerUpiId;
	
	private String transactionId;
	// private byte[] sahyogReceiptImage;
	
//	@ManyToOne
//	@JoinColumn(name = "userPaymentsDetails")
//	private User userPayments;
//	
//	@OneToMany(mappedBy = "paymentsImage",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	private Set<FileEntity> fileEntity;
	
	@ManyToOne
	//@JoinTable(name="linked_TBL")
	@JoinColumn(name = "userPaymentsDetails")
	private User userPayments;
	
	//@OneToMany(mappedBy = "paymentsImage",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<FileEntity> fileEntity;
	
}
