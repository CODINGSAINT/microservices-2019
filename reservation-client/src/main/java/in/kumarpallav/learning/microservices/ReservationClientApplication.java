package in.kumarpallav.learning.microservices;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.Getter;
import lombok.Setter;

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
@EnableCircuitBreaker
@EnableBinding(Source.class)
public class ReservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

}


@Getter
@Setter
class Reservation{
	private String name;
}


@Configuration
class ReservationClientConfig{
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		 return new RestTemplate();
	}
}
@RestController
@RequestMapping("reservations")
class ReservationClientController{
	@Autowired 
	private RestTemplate restTempate;
	
	@Autowired
	Source source;

	public Collection <String> getReservationFallback(){
		return Stream.of("Pallav").collect(Collectors.toList());
	}
	@PostMapping("reservation")
	public void addReservation(@RequestBody Reservation reservation) {
		
		source.output().send(MessageBuilder.withPayload(reservation).build());
	}
	 
	@GetMapping("names")
	@HystrixCommand(fallbackMethod = "getReservationFallback")
	public Collection <String>getReservations() {

		ParameterizedTypeReference<Resources<Reservation>> ptr = new ParameterizedTypeReference<Resources<Reservation>>() {
		};
		ResponseEntity<Resources<Reservation>> response = restTempate
				.exchange("http://RESERVATION-SERVICE/reservations", 
						HttpMethod.GET, null, ptr);
		return response.getBody()
				.getContent()
				.stream()
				.map(Reservation::getName)
				.collect(Collectors.toList());
	}
	
}