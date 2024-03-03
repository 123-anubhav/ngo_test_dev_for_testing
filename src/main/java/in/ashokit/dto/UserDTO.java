package in.ashokit.dto;

import java.sql.Blob;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Integer userId;

	private Set<String> userRoles;

	private String userName;
	private String userFatherName;
	private String husbandName;
	private String mobileNumber;
	private String emailId;
	private String gender;

	// @DateTimeFormat(pattern = "mm-dd-yyyy")
	private Date dob;
	
	// PLAN VALIDITY
	private LocalDate planValidity;

	private boolean iAgreeRules = false;
	private Blob payImage;
	private boolean paystatus;
	private String aliveOrDead;
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
	private Byte selfie;

	// NOMINEE DETAILS
	private String nomineeName;
	private String nomineeAccountNumber;
	private String nomineeIFSCCode;
	private String nomineeBranchName;
	private String nomineeBankName;

}
