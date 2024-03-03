package in.ashokit.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
//@Table(name="ngo_transaction_image_details")
public class FileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;

	@Lob
	private byte[] data;
/*	
	@ManyToOne
	@JoinColumn(name = "payment_images_details")
	private PaymentDetails paymentsImage;
*/
}
