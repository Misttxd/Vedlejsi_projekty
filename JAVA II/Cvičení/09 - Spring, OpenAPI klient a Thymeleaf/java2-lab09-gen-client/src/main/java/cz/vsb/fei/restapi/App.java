package cz.vsb.fei.restapi;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.PlayerControllerApi;

import lombok.extern.log4j.Log4j2;

/**
 *  Class <b>App</b> - main class
 *  @author     Java I
 */
@Log4j2
public class App {

	public static void main(String[] args) {
		log.info("Launching Java application.");

		//Usage example
		PlayerControllerApi api = new PlayerControllerApi();
		api.setCustomBaseUrl("http://localhost:8080");

		try {
			api.getAll().forEach(score -> log.info(score.toString()));
		} catch (ApiException e) {
			log.error("Chyba komunikace", e);
		}
	}
	
}