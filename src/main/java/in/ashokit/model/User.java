package in.ashokit.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
//@Table(name = "ngo_user_details")
public class User {

	@Id
	// @Column(name="uid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles_tab", joinColumns = @JoinColumn(name = "userId"))
	@Column(name = "urole")
	private Set<String> userRoles;

//	@OneToMany(mappedBy = "userPayments", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	private List<PaymentDetails> paymentDetails;

	@OneToMany(/* mappedBy = "userPayments", */ cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<PaymentDetails> paymentDetails;

	// PLAN VALIDITY
	private LocalDate planValidity;

	private String userName;
	private String userFatherName;
	private String husbandName;
	private String mobileNumber;
	private String emailId;
	private String gender;

	// @DateTimeFormat(pattern = "mm-dd-yyyy")
	private Date dob;

	private boolean iAgreeRules = false;
	// private Blob payImage;
	private boolean paystatus;
	// private String aliveOrDead;
	private String isAlive;
	private String adharNumber;

	// ADDED EXTRA
	private String bloodgroup;

	// FOR PERMANENT ADDRESS
	private String flatNumber;
	private String gali;
	private String mohalla;
	private String nivas;
	private String block;
	private String ward;
	private String thana;
	private String nearFamousPlace;
	private String janpad;

	// FOR TEMPORARY ADDRESS
	private String flatNumber1;
	private String gali1;
	private String mohalla1;
	private String nivas1;
	private String block1;
	private String ward1;
	private String thana1;
	private String nearFamousPlace1;
	private String janpad1;

	private String jobName;
	private String workPlaceExist;
	private String workAddress;

	private String maritalstatus;

	private String whatsappMobileNumber;

	private String password;
	private String confirmPassword;

	// UPLOAD SELFIE
	// private Byte selfie;

	// NOMINEE DETAILS
	private String nomineeName;
	private String nomineeAccountNumber;
	private String nomineeIFSCCode;
	private String nomineeBranchName;
	private String nomineeBankName;
}
