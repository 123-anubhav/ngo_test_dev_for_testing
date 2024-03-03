package in.ashokit.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import in.ashokit.dto.EnquiryForm;
import in.ashokit.dto.FileEntityDTO;
import in.ashokit.dto.NomineeDetails;
import in.ashokit.dto.PaymentStatus;
import in.ashokit.dto.UserDTO;
import in.ashokit.model.PaymentDetails;
import in.ashokit.model.User;
import in.ashokit.repo.PaymentDetailsRepo;
import in.ashokit.repo.UserRepository;
import in.ashokit.service.FileService;
import in.ashokit.service.IUserService;
import in.ashokit.util.EmailSenderService;

@Controller
public class UserController {

	@Autowired
	private IUserService service;

	private String sessionUser = "ROLE_CUSTOMER";

	@Autowired
	private UserRepository repo;

	@Autowired
	private FileService fileService;

	@Autowired
	private PaymentDetailsRepo paymentDetailsRepo;

	@GetMapping("/home")
	public String showHome(HttpSession session, Model m) {
		System.out.println("Controller.home()");
		// Optional<User> findByIsAlive = repo.findByIsAlive();
		List<User> findAll = repo.findAll();
		// System.out.println(findAll.get(0).getUserId()+" ::
		// "+findAll.get(0).getIsAlive());
		List<User> dataIsNotAlive = new ArrayList<User>();
		for (int i = 0; i < findAll.size(); i++)
			if (findAll.get(i).getIsAlive() != null)
				if (findAll.get(i).getIsAlive().contains("no")) {
					dataIsNotAlive.add(findAll.get(i));
					System.out.println("dataValue :: " + findAll.get(i));
				}

		m.addAttribute("dataValue", dataIsNotAlive);

		// session.invalidate();
		return "home";
	}

	@GetMapping("/hello")
	public String showHello() {
		return "hello";
	}

	@GetMapping("/admin")
	// @ResponseBody
	public String showAdmin() {
		return "admin";
	}

	@GetMapping("/customer")
	@ResponseBody
	public String showCustomer() {
		return "customer";
	}

	@GetMapping( "/login" )
	public String showLogin() {
		System.out.println("UserController.showLogin()");
		return "login";
	}
	
	

	@GetMapping("/register")
	public String showReg(Model model) {
//		String paymentStatus = PaymentController.getPaymentStatus();
//			//m.addAttribute("message", "pay success");
//		if(paymentStatus.getPaymentStatus()==null) {
//			model.addAttribute("message", "आपने भुगतान नहीं किया है इसलिए आप हमारे सेल्फकेयर के साथ पंजीकरण नहीं कर सकते। <br> कृपया पुनः प्रयास करें");
//			//return "redirect:/ register";
//		}
//		else
//		model.addAttribute("message", "user successfully registered please login to check!..."+paymentStatus.getPaymentStatus());

		return "register";
	}

	@Autowired
	private PaymentStatus paymentStatus;

	@Autowired
	private UserDTO userDTO;

	@Autowired
	private FileEntityDTO fileEntityDTO;

	@PostMapping("/save")
	public String saveUser(@ModelAttribute User user, /* MultipartFile file, */ HttpSession session, Model model)
			throws IOException {

		Set<String> userRoles = user.getUserRoles();
		if (userRoles.contains("ADMIN")) {
			sessionUser = "ROLE_ADMIN";
			System.out.println("if block executes :; userRoles.contains(\"ADMIN\")" + userRoles.contains("ADMIN"));
			session.setAttribute("getUserRole", "ROLE_ADMIN ");
		} else {
			sessionUser = "ROLE_CUSTOMER";
			session.setAttribute("getUserRole", "ROLE_CUSTOMER ");
		}

		// System.out.println("uploaded file is :: "+file.getOriginalFilename());
		userDTO.setMobileNumber(user.getMobileNumber());
		userDTO.setPassword(user.getPassword());
		
		userDTO.setDob(user.getDob());
		
		LocalDate currentDate = LocalDate.now();
		System.out.println("curent date from /save controller is\n******************"+currentDate);
		user.setPlanValidity(currentDate);
		userDTO.setPlanValidity(user.getPlanValidity());
		
		userDTO.setAdharNumber(user.getAdharNumber());
		userDTO.setConfirmPassword(user.getConfirmPassword());
		userDTO.setEmailId(user.getEmailId());
		userDTO.setGender(user.getGender());
		userDTO.setHusbandName(user.getHusbandName());
		userDTO.setJobName(user.getJobName());
		userDTO.setNomineeAccountNumber(user.getNomineeAccountNumber());
		userDTO.setNomineeBankName(user.getNomineeBankName());
		userDTO.setNomineeBranchName(user.getNomineeBranchName());
		userDTO.setNomineeIFSCCode(user.getNomineeIFSCCode());
		userDTO.setNomineeName(user.getNomineeName());
		userDTO.setWhatsappMobileNumber(user.getWhatsappMobileNumber());
		userDTO.setUserFatherName(user.getUserFatherName());
		userDTO.setUserName(user.getUserName());
		userDTO.setUserRoles(user.getUserRoles());
		userDTO.setWorkPlaceExist(user.getWorkPlaceExist());
		userDTO.setWorkAddress(user.getWorkAddress());

		// set predefined roles as customer

		Set<String> roles = new HashSet<>();
		roles.add("ROLE_CUSTOMER");
		userDTO.setUserRoles(roles);

//		fileEntityDTO.setFileName(file.getOriginalFilename());
//		fileEntityDTO.setData(file.getBytes());

		System.out.println("paymentStatus is :: " + paymentStatus.getPaymentStatus());

//		if(paymentStatus.getPaymentStatus()!=null) {
//			try {
//				fileService.saveFile(file);
//				System.out.println(
//						"fileService.saveFile(file) :: executes success\nuser.getUserRoles():: " + user.getUserRoles());
//
//			} catch (Exception e) {
//				return "Failed to upload file";
//			}
//			Integer id = service.saveUser(user);
//			String message = "User '" + id + "' created";
//			model.addAttribute("message", "user successfully registered please login to check!..."+paymentStatus.getPaymentStatus());
//		}				
		return "redirect:/register";
	}

	@GetMapping("/testSession/{name}")
	public String testSession(@PathVariable String name, HttpServletRequest request, HttpSession session) {
		System.out.println("HomeController.testSession()" + name);
		session = request.getSession();
		session.setAttribute("name", name);
		System.out.println("============session===+=" + session);
		// session.setAttribute("name", name);
		return "testSession";
	}

	@GetMapping("/about")
	public String about() {
		System.out.println("HomeController.about()");
		return "about";
	}

	@GetMapping("/myProfile")
	public String myProfile() {
		System.out.println("HomeController.myProfile()");
		return "myProfile";
	}

	@GetMapping("/searchById")
	//@ResponseBody
	public  String searchById(@RequestParam("emailId") String emailId,
			/* @RequestParam int mobileNumber, */ Model m) {/*
															 * Optional<UserDetails> findById =
															 * userDetailsRepo.findById(mobileNumber );
															 * m.addAttribute("myDonation", findById.get());
															 */
		System.out.println("HomeController.searchById()");
		Optional<User> findByEmailId = repo.findByEmailId(emailId);
		System.out.println("findByEmailId.get():: *******************   ::"+findByEmailId.get());
		//List<User> findById = repo.findAll();
		/*
		 * int id=1; Optional<UserDetails> findById = userDetailsRepo.findById(id);
		 * if(findById.isPresent()) m.addAttribute("findById", findById);
		 */
		m.addAttribute("findById", findByEmailId.get()); // not working bug here 
		//return findByEmailId.get();
		return "searchById";
	}

	@GetMapping("/donateNow")
	public String donateNow(Model m) {
		System.out.println("HomeController.donateNow()");
		List<User> findById = repo.findAll();
		m.addAttribute("findById", findById);
		return "donateNow";
	}

	@GetMapping("/viewDonars")
	public String viewDonars(Model m) {
		System.out.println("HomeController.viewDonars()");
		List<User> findById = repo.findAll();
		m.addAttribute("findById", findById);
		return "viewDonar";
	}
	
	@GetMapping("/vyasthaSulkSearchByNumber")
	public String viewDvyasthaSulkSearchByNumberonars() {
		System.out.println("HomeController.vyasthaSulkSearchByNumber()");
		
		return "vyasthaSulkSearchByNumber";
	}

	@GetMapping("/vyasthaSulk")
	public String vyasthaSulk(@RequestParam("emailId") String emailId,Model m) {
		// todo: yha me login k hisab se session usermobile number se date plan lana h
		System.out.println("HomeController.vyasthaSulk()");
		//List<User> findById = repo.findAll();
		Optional<User> findByEmailId = repo.findByEmailId(emailId);
		User userByMobileNumber = findByEmailId.get();
		
		
		LocalDate planValidity = userByMobileNumber.getPlanValidity();//findById.get(31).getPlanValidity();
		
		int lengthOfYear = planValidity.lengthOfYear();
		
		System.out.println("planValidity FULL DATE COMING :: "+ planValidity+"\nlengthOfYear :: "+lengthOfYear);
		//m.addAttribute("findById", findById);
		m.addAttribute("findByIdDateCounter", planValidity);

		LocalDate oneYearAfter = planValidity.plusYears(1);
		System.out.println("oneYearAfter :: "+oneYearAfter);//current date + 1years => date coming
		m.addAttribute("planValidityExtended", oneYearAfter);
		
		LocalDate currentDate = LocalDate.now();
		long differenceInDays = ChronoUnit.DAYS.between( currentDate,oneYearAfter);//366-366
        
		// Convert the difference to an int if needed
        int differenceInDaysInt = Math.toIntExact(differenceInDays);
        
        // Print the result
        System.out.println("differenceInDays"+differenceInDays+"Difference in days: " + differenceInDaysInt);//0
		
		m.addAttribute("planValidityNumberOfDays", differenceInDaysInt);
		return "vyasthaSulk";
	}

	@GetMapping("/transferedByDeadPerson")
	public String transferedByDeadPerson(Model m) {
		System.out.println("HomeController.transferedByDeadPerson()");
		List<User> findById = repo.findAll();
		/*
		 * int id=1; Optional<UserDetails> findById = userDetailsRepo.findById(id);
		 * if(findById.isPresent()) m.addAttribute("findById", findById);
		 */
		m.addAttribute("findById", findById);

		return "transferedByDeadPerson";
	}

	@GetMapping("/donation")
	public String donation(Model m) {
		List<User> findAll = repo.findAll();
		m.addAttribute("findAllDonationList", findAll);
		System.out.println("HomeController.donation()");
		return "donation";
	}
	
		
	@GetMapping("/donationById/{userId}")
	public String donationByMobileNumber(@PathVariable Integer userId, Model m) {
		Optional<User> findById = repo.findById(userId);
		m.addAttribute("findAllDonationList", findById.get());
		
		String userMobileNumber = findById.get().getEmailId();
		System.out.println("userMobileNumber :: "+userMobileNumber);
		Set<PaymentDetails> paymentDetailFindbyMobileNumber = paymentDetailsRepo.findByMobileNumber(userMobileNumber);
		System.out.println("payment data is :: "+ paymentDetailFindbyMobileNumber);
		m.addAttribute("msg", "msg jhhjh");
		m.addAttribute("paymentDetailFindbyMobileNumber", paymentDetailFindbyMobileNumber);
			return "donation";
	}
	
	@GetMapping("/nomineeById/{userId}")
	public String nomineeDetails(@PathVariable Integer userId, Model m) {
		Optional<User> findById = repo.findById(userId);
		
		User userData = findById.get();
		
		NomineeDetails nomineeDetailsByID = new NomineeDetails();
		nomineeDetailsByID.setUserName(userData.getUserName());
		nomineeDetailsByID.setNomineeName(userData.getNomineeName());
		nomineeDetailsByID.setNomineeAccountNumber(userData.getNomineeAccountNumber());
		nomineeDetailsByID.setNomineeBankName(userData.getNomineeBankName());
		nomineeDetailsByID.setNomineeBranchName(userData.getNomineeBranchName());
		nomineeDetailsByID.setNomineeIFSCCode(userData.getNomineeIFSCCode());
		System.out.println("nominee details are "+nomineeDetailsByID+"with id :"+userData.getUserId());
		
		m.addAttribute("findAllDonationList", nomineeDetailsByID);	
		m.addAttribute("msg", "msg jhhjh");
			return "nomineeById";
	}

	@GetMapping("/allDoantionList")
	public String allDonationList(Model m) {
		List<User> findAll = repo.findAll();
		m.addAttribute("findAllDonationList", findAll);
		System.out.println("HomeController.allDonationList()");
		return "allDonationList";
	}

	@GetMapping("/faq")
	public String faq() {
		System.out.println("HomeController.faq()");
		return "faq";
	}

	@GetMapping("/error")
	public String error() {
		System.out.println("HomeController.error()");
		return "error";
	}

	@GetMapping("/loginHeader")
	public String loginHeader() {
		System.out.println("HomeController.loginHeader()");
		return "loginHeader";
	}

	@GetMapping("/donateMoney")
	public String donateMoney(Model m) {
		System.out.println("HomeController.donateMoney()");
		m.addAttribute("success", "you donateMoney successfully....");
		return "donateMoney";
	}

	@GetMapping("/enquiry")
	public String enquiry() {
		System.out.println("HomeController.enquiry()");
		return "enquiry";
	}

	@Autowired
	private EmailSenderService senderservice;

	@PostMapping("/enquiryPost")
	public String enquiryPost(@ModelAttribute EnquiryForm enquiry, Model m) {
		// String subject = "hi, i am boot email with otp module to send you otp for
		// varification purpose\n otp is: ";
		senderservice.sendMail(enquiry.getEmailId(), enquiry.getName(), enquiry.getQuery());

		System.out.println("HomeController.enquiryPost() data is :: " + enquiry);
		m.addAttribute("message", "हमसे परामर्श करने के लिए धन्यवाद, हम जल्द ही आप तक पहुंचेंगे!....");
		// return "mail send successfully";
		return "enquiry";
	}

	@GetMapping("/main-page")
	public String header() {
		System.out.println("HomeController.main-page()");
		return "mainPage";
	}

	@GetMapping("/myDonationList")
	public String myDonationList(
			/* @RequestParam int mobileNumber, */ Model m) {/*
															 * Optional<UserDetails> findById =
															 * userDetailsRepo.findById(mobileNumber); if
															 * (findById.isPresent()) m.addAttribute("myDonation",
															 * findById.get()); else m.addAttribute("myDonation",
															 * "data not found");
															 */
		List<User> findAll = repo.findAll();
		m.addAttribute("data", findAll);

		System.out.println("HomeController.myDonationList()");
		return "myDonationList";
	}

	@GetMapping("/otherDonationList")
	public String otherDonationList(Model m) {
		List<User> findAll = repo.findAll();
		m.addAttribute("data", findAll);
		System.out.println("HomeController.otherDonationList()");
		return "otherDonationList";
	}

	@GetMapping("/fillFormAfterPayment")
	public String fillFormAfterPayment() {
		System.out.println("HomeController.fillFormAfterPayment()");
		return "formAfterPayment";
	}

	@GetMapping("/success")
	public String success() {
		System.out.println("HomeController.success()");
		return "success";
	}

	@GetMapping("/denied")
	public String denied() {
		System.out.println("HomeController.denied()");
		return "denied";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/login?logout";
	}

//	@GetMapping("/")
//	public String baseUrl() {
//		return "loginSuccess";
//	}
	
	@GetMapping( "/" )
	public String successLogin() {
		System.out.println("UserController.successLogin()");
		return "loginSuccess";
	}
	
	@GetMapping( "/loginSuccess" )
	public String afterSuccessLogin() {
		System.out.println("UserController.afterSuccessLogin()");
		return "loginSuccess";
	}
	
	@PostMapping("/loginSuccess")
	public String loginSuccess(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession session) {
		System.out.println("HomeController.loginSuccess() ::" + password + " ::username " + username);
		session.setAttribute("username", username);
		// Set<String> userRoles = user.getUserRoles();
		if (sessionUser.equalsIgnoreCase("ROLE_ADMIN")) {
			System.out.println("if block executes :; userRoles.contains(\"ADMIN\")");
			session.setAttribute("getUserRole", "ROLE_ADMIN ");
		}
//		else
//			session.setAttribute("getUserRole", "ROLE_CUSTOMER ");
		// create login success page jsut like loginHome
		return "loginSuccess";
	}

	@GetMapping("/deleteRecord")
	public String deleteRecord(/* @PathVariable String id */) {
		// delete record jpa method create and call
		System.out.println("HomeController.deleteRecord()");
		return "deleteRecord";
	}

	// post method add krna h
	//todo: bug here img not upload 
	@PostMapping(value = "/fillFormAfterPaymentPost")
	public String savefill(@ModelAttribute PaymentDetails paymentDetails, MultipartFile file, HttpSession session,
			Model m) throws Exception {
		System.out.println("UserController.savefill()::  paymentDetails ::" + paymentDetails + "\nfile is :: " + file);
		// Tasks: FILE DATA NULL LE RHA H BUG
		String originalFilename = file.getOriginalFilename();
		
		if (file != null) {
			try {
				System.out.println("HomeController.fillFormAfterPayment :: fileService.saveFile(file) :: try executes");
				fileService.saveFile(file);
				System.out.println("fillFormAfterPayment :: fileService.saveFile(file) :: executes success");
				System.out.println("  paymentDetailsRepo.save(paymentDetails) .... starts ");
				PaymentDetails save = paymentDetailsRepo.save(paymentDetails);
				System.out.println("HomeController.formFillAfterPayment()" + paymentDetails);
			} catch (Exception e) {
				return "Failed to upload file";
			}
			m.addAttribute("status", "डेटा सफलतापूर्वक अपलोड हो गया है");
		} else {
			System.out.println(" MultipartFile is null :: " + file+"status is :: ");
			
				m.addAttribute("status",
						"अपलोड विफल। भुगतान फोटो के साथ अपलोड करना आवश्यक है -> bug here not upload img in server");
			
			
		}

		// return "redirect:/fillFormAfterPayment";
		return "formAfterPayment";
	}

//	@GetMapping("/donateNow")
//	// @ResponseBody
//	public String donateNow() {
//		System.out.println("HomeController.donateNow()");
//
//		return "donateNow";
//	}

	@GetMapping("/user")
	@ResponseBody
	public String user() {
		return "user";
	}

}