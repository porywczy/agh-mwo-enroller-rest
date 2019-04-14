package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

//kontroler do endpointu Particippant
@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	// spring wstrzykuje serwis; musi sie zgadzac nazwa i typ
	// dzialanie komponentow
	@Autowired
	ParticipantService participantService;

	// mapowanie zasobow i funkcji
	// endpoint = sklejanie sciezki: klasy + metody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants() {

		// z serwisu by hibernate
		Collection<Participant> participants = participantService.getAll();

		// dajemy kolekcje obiektow i status: OK
		// przez Jackson tworzony JSON i send do klienta
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	// endpointa w klasie ParticipantRestController
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	// Zaimplementuj dodawanie uczestników. Metoda powinna zostać zadeklarowana
	// w sposób następujący:
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
	   if (foundParticipant != null) {
	         return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
	     }
	   participantService.add(participant);
	   return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}
	
	//zaimplementuj usuwanie uczestników
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participantService.delete(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK); // NO_CONTENT
	}
	
	//zaimplementuj aktualizację
	//zmiana password only
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant incomingParticipant) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participant.setPassword(incomingParticipant.getPassword());
	   participantService.update(participant);
	   return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}
	

}
