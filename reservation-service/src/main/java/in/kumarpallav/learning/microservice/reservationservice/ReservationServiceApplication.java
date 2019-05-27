package in.kumarpallav.learning.microservice.reservationservice;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@EnableEurekaClient
@EnableBinding(Sink.class)
@SpringBootApplication 
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}
	
}

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Reservation{
	public Reservation(String n) {
		this.name=n;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long reservationId;
	private String name;
}

@RestController
 class ReservtionController{
	@Autowired
	private ReservationRepository reservationRepository;
	
	@GetMapping("reservation")
	public Collection<Reservation> getReservation(){
		return reservationRepository.findAll();
	}
	
	@StreamListener(target = Sink.INPUT)
	public void processCheapMeals(String message) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper= new ObjectMapper();
	Reservation reservation= mapper.readValue(message,Reservation.class);
	    System.out.println("This was a great message!: "+reservation);
	    reservationRepository.save(reservation);
	}
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation , Long>{
	@RestResource(path="by-name")
	Collection<Reservation> findByName(@Param("name")String name);
}
@Component
class CLR implements CommandLineRunner{
@Autowired
private ReservationRepository reservationRepository;
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		Stream.of("Pallav","Arachana")
		.forEach(n->{
			reservationRepository.save(new Reservation(n));
		});
				 
	}
	
}
