package in.ashokit.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
*/
import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import in.ashokit.dto.FileEntityDTO;
import in.ashokit.dto.PaymentStatus;
import in.ashokit.dto.UserDTO;
import in.ashokit.model.User;
import in.ashokit.repo.UserRepository;
import in.ashokit.service.FileService;
import in.ashokit.service.IUserService;

@Controller
public class PaymentController {

	@Value("${stripe.apiKey}")
	private String stripeApiKey;

	/*
	 * private static String paymentStatus=null;
	 * 
	 * public static String getPaymentStatus() { return paymentStatus; }
	 * 
	 * public static void setPaymentStatus(String paymentStatus) { paymentStatus =
	 * paymentStatus; }
	 */
	@Autowired
	private PaymentStatus paymentStatus;

	@Autowired
	private UserRepository repo;

	@Autowired
	private FileService fileService;

	@Autowired
	private IUserService service;

	@Autowired
	private UserDTO userDTO;

	@Autowired
	private FileEntityDTO fileEntityDTO;

	@PostMapping("/processPayment")
	public String processPayment(@RequestParam("stripeToken") String token, @RequestParam("amount") int amount,
			Model model) {
		Stripe.apiKey = stripeApiKey;

		try {
			Stripe.apiKey = "sk_test_51NjOJBSFx3mRYZppOhIR57Lx1g6XUpFLQty5ElxFVhHDbMKYu44YSPx8qHP6pOv6i6fCvVJpaGn6sxB1iuA2Xdvt00uTg1FEOZ";

			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount(amount * 100L) // it is
																											// 1099L
																											// means
																											// ₹10.99INR
																											// i.e.
																											// 10*10
																											// =100 ->
																											// 1.00 inr
					.setCurrency("inr").setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
					.build();

			System.out.println("PaymentController.processPayment() payment starts");

			String status = null;
			PaymentIntent paymentIntent = null;
			try {
				paymentIntent = PaymentIntent.create(params);
				// setPaymentStatus("Payment of "+amount+" is successfully transfered.. now you
				// can submit fom and register with us...");
				System.out.println("payment response from stripe is :: \n PaymentIntent is :: \n " + paymentIntent);
				status = paymentIntent.getStatus();

				System.out.println("PaymentController.processPayment() payment ends::status" + status);

				/*
				 * actual live payment status check logic if ("succeeded".equals(status)) {
				 * model.addAttribute("paymentStatus", "Payment succeeded"); } else if
				 * ("failed".equals(status)) { model.addAttribute("paymentStatus",
				 * "Payment failed"); }
				 */
			}

			catch (CardException e) {
				// Card was declined or there was an issue with the card
				String declineCode = e.getCode();
				System.out.println("declineCode :: " + declineCode);
				if ("card_declined".equals(declineCode)) {
					// Card was declined for some reason
					String declineMessage = e.getMessage();
					System.out.println("declineMessage :: " + declineMessage);
					// Log or handle the decline message appropriately
				} else if ("insufficient_funds".equals(declineCode)) {
					// Card has insufficient funds
					// Handle insufficient funds scenario
					return "insufficientBalance";
				} else if ("stolen_card".equals(declineCode)) {
					// Card is marked as stolen
					// Handle stolen card scenario
					return "stolenCard";
				} else {
					// Handle other decline scenarios
				}
			} catch (StripeException e) {
				// Handle other types of Stripe exceptions
				// For example, network issues, API errors, etc.
				e.printStackTrace();
				model.addAttribute("message", "Payment failed. Please try again.");
			} catch (Exception e) {
				// Handle other generic exceptions
			}

			System.out.println("\nuserDTO is mobile number ( email field) is " + userDTO.getEmailId()
					+ "\nuserDTO password" + userDTO.getPassword());

			User user = new User();
			// TODO : HERE create all user class setter and store values
			user.setMobileNumber(userDTO.getMobileNumber());
			user.setPassword(userDTO.getPassword());
			
			
			user.setDob(userDTO.getDob());
			user.setPlanValidity(userDTO.getPlanValidity());
			System.out.println("PaymentController.processPayment():: :: "+user.getPlanValidity());
			
			user.setAdharNumber(userDTO.getAdharNumber());
			user.setConfirmPassword(userDTO.getConfirmPassword());
			user.setEmailId(userDTO.getEmailId());
			user.setGender(userDTO.getGender());
			user.setHusbandName(userDTO.getHusbandName());
			user.setJobName(userDTO.getJobName());
			user.setNomineeAccountNumber(userDTO.getNomineeAccountNumber());
			user.setNomineeBankName(userDTO.getNomineeBankName());
			user.setNomineeBranchName(userDTO.getNomineeBranchName());
			user.setNomineeIFSCCode(userDTO.getNomineeIFSCCode());
			user.setNomineeName(userDTO.getNomineeName());
			user.setWhatsappMobileNumber(userDTO.getWhatsappMobileNumber());
			user.setUserFatherName(userDTO.getUserFatherName());
			user.setUserName(userDTO.getUserName());

			user.setUserRoles(userDTO.getUserRoles()); // setting role as role_customer

			user.setWorkPlaceExist(userDTO.getWorkPlaceExist());
			user.setWorkAddress(userDTO.getWorkAddress());

			Integer id = service.saveUser(user);
			String message = "User '" + id + "' created";
			System.out.println("data saved in db usernew id is ::" + message);
			System.out.println("status is :: " + status);

			System.out.println("fileEntityDTO.getFileName() :: " + fileEntityDTO.getFileName());

			if (status.contains("requires_payment_method")) {
				try {
					// fileService.saveFile(fileEntityDTO); --> not needed in register b'cos payment
					// integration implemented
					System.out.println("fileService.saveFile(file) :: executes success\nuser.getUserRoles():: "
							+ userDTO.getUserRoles());

				} catch (Exception e) {
					return "Failed to upload file";
				}
				// model.addAttribute("message","user successfully registered please login to
				// check!..." + paymentStatus.getPaymentStatus());
				// और अब आपने हमारे साथ सफलतापूर्वक पंजीकरण कर लिया है
				String messageSuccess = user.getUserName()
						+ ", भुगतान सफल हो गया | अब आप हमारी सेवाओं तक पहुँच सकते हैं";
				model.addAttribute("message", messageSuccess);
				model.addAttribute("status", status);
				model.addAttribute("paymentIntent_data_from_stripe_is", paymentIntent);
				model.addAttribute("user_data_is", userDTO);
			}

			else {
				model.addAttribute("message", "Payment failed.. try again");
				model.addAttribute("status", status);
				model.addAttribute("paymentIntent_data_from_stripe_is", paymentIntent);

			}
			// paymentStatus.setPaymentStatus(status);

			System.out.println("\nPaymentController Status of payment is ::" + status);
		} catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("token is ::" + token + "\n Amount is :: " + amount);
		// return "redirect:/register";
		return "paymentResult";
	}

	@GetMapping("paymentForm")
	public String paymentForm() {
		return "paymentForm";
	}
	
	//ye baeldung ka hai
	@GetMapping("paymentFormBaeldung")
	public String paymentFormBaeldung() {
		return "paymentFormBaeldung";
	}
	
	// FOR PLAN VALIDITY EXTEND CODE 
	@GetMapping("payVyasthaSulk")
	public String payVyasthaSulk() {
		return "payVyasthaSulk";
	}

	@GetMapping("paymentFormPlanValidityExtend")
	public String paymentFormPlanValidityExtend() {
		return "paymentFormPlanValidityExtend";
	}

	@PostMapping("/processPaymentPlanValidityExtend")
	public String processPaymentPlanValidityExtend(@RequestParam("emailId")String emailId
					,@RequestParam("stripeToken") String token,
					@RequestParam("amount") int amount,
					Model model) {
		Stripe.apiKey = stripeApiKey;

		try {
			Stripe.apiKey = "sk_test_51NjOJBSFx3mRYZppOhIR57Lx1g6XUpFLQty5ElxFVhHDbMKYu44YSPx8qHP6pOv6i6fCvVJpaGn6sxB1iuA2Xdvt00uTg1FEOZ";

			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount(amount * 100L) // it is
																											// 1099L
																											// means
																											// ₹10.99INR
																											// i.e.
																											// 10*10
																											// =100 ->
																											// 1.00 inr
					.setCurrency("inr").setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
					.build();

			System.out.println("PaymentController.processPayment() payment starts");

			String status = null;
			PaymentIntent paymentIntent = null;
			try {
				paymentIntent = PaymentIntent.create(params);
				// setPaymentStatus("Payment of "+amount+" is successfully transfered.. now you
				// can submit fom and register with us...");
				System.out.println("payment response from stripe is :: \n PaymentIntent is :: \n " + paymentIntent);
				status = paymentIntent.getStatus();

				System.out.println("PaymentController.processPayment() payment ends::status" + status);

				/*
				 * actual live payment status check logic if ("succeeded".equals(status)) {
				 * model.addAttribute("paymentStatus", "Payment succeeded"); } else if
				 * ("failed".equals(status)) { model.addAttribute("paymentStatus",
				 * "Payment failed"); }
				 */
			}

			catch (CardException e) {
				// Card was declined or there was an issue with the card
				String declineCode = e.getCode();
				System.out.println("declineCode :: " + declineCode);
				if ("card_declined".equals(declineCode)) {
					// Card was declined for some reason
					String declineMessage = e.getMessage();
					System.out.println("declineMessage :: " + declineMessage);
					// Log or handle the decline message appropriately
				} else if ("insufficient_funds".equals(declineCode)) {
					// Card has insufficient funds
					// Handle insufficient funds scenario
					return "insufficientBalance";
				} else if ("stolen_card".equals(declineCode)) {
					// Card is marked as stolen
					// Handle stolen card scenario
					return "stolenCard";
				} else {
					// Handle other decline scenarios
				}
			} catch (StripeException e) {
				// Handle other types of Stripe exceptions
				// For example, network issues, API errors, etc.
				e.printStackTrace();
				model.addAttribute("message", "Payment failed. Please try again.");
			} catch (Exception e) {
				// Handle other generic exceptions
			}

			
			Optional<User> findByEmailId = repo.findByEmailId(emailId);
			User user = findByEmailId.get();
			//Integer userId = user.getUserId();
			LocalDate planValidity = user.getPlanValidity();
			
			LocalDate extendedValidity = planValidity.plusYears(1); //validity extended for 1 year date 
			user.setPlanValidity(extendedValidity);
			repo.save(user);
					
			if (status.contains("requires_payment_method")) {
				
					System.out.println("PaymentController.processPaymentPlanValidityExtend()::inside try block::");
				String messageSuccess = user.getUserName()
						+ ", भुगतान सफल हो गया | अब आप हमारी सेवाओं तक पहुँच सकते हैं|validity extended for 1 year...";
				model.addAttribute("message", messageSuccess);
				model.addAttribute("status", status);
				model.addAttribute("paymentIntent_data_from_stripe_is", paymentIntent);
				model.addAttribute("user_data_is", userDTO);
			}

			else {
				model.addAttribute("message", "Payment failed.. try again");
				model.addAttribute("status", status);
				model.addAttribute("paymentIntent_data_from_stripe_is", paymentIntent);

			}
			// paymentStatus.setPaymentStatus(status);

			System.out.println("\nPaymentController Status of payment is ::" + status);
		} catch (Exception e) {
			e.printStackTrace();

		}
		System.out.println("token is ::" + token + "\n Amount is :: " + amount);
		
		return "paymentFormPlanValidityExtend";
	}

}


