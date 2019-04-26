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

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

//kontroler do endpointu Meetings
@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	// spring wstrzykuje serwis; musi sie zgadzac nazwa i typ
	// dzialanie komponentow
	@Autowired
	ParticipantService participantService;
	
	@Autowired
	MeetingService meetingService;

	// mapowanie zasobow i funkcji
	// endpoint = sklejanie sciezki: klasy + metody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {

		// z serwisu by hibernate
		Collection<Meeting> meetings = meetingService.getAll();

		// dajemy kolekcje obiektow i status: OK
		// przez Jackson tworzony JSON i send do klienta
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	// endpoint w klasie MeetingRestController
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	// Pobieranie uczestników spotkania
	// pobrac spotkanie
	// pobrac uczestnika
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipantsFromMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		//zwraca obiekt encyjny Meeting
//		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
		
		//get liste obiektow 
		//z serwisu by hibernate
		Collection<Participant> participants = meeting.getParticipants();
	
		// zwraca kolekcje obiektow encyjnych Participant i status: OK
		// przez Jackson tworzony JSON i send do klienta
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	// dodawanie uczestników. Metoda powinna zostać zadeklarowana
	// w sposób następujący:
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
//		Meeting foundMeeting = meetingService.findById(meeting.getId());
//	   if (foundMeeting != null) {
//	         return new ResponseEntity("Unable to create. A meeting with id " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
//	     }
	   meetingService.add(meeting);
	   return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}
	
//	Dodawanie uczestnika do spotkania
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @RequestBody Participant participant) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
	   if (foundParticipant != null) {
	         return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
	     }
	 //na danym obiekcie biznesowym robimy addParticipant i save
	   meeting.addParticipant(participant);
	   return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
 
	   
	   //1 strzal == 1 uczestnik; bez kolekcji
	}
	
//	@RequestMapping(value = "", method = RequestMethod.POST)
//	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
//		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
//	   if (foundParticipant != null) {
//	         return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
//	     }
//	   participantService.add(participant);
//	   return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
//	}
	
	//zaimplementuj usuwanie uczestników
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK); // NO_CONTENT
	}
	
	//zaimplementuj aktualizację
	//zmiana password only
//	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
//	public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant incomingParticipant) {
//		Participant participant = participantService.findByLogin(login);
//		if (participant == null) {
//			return new ResponseEntity(HttpStatus.NOT_FOUND);
//		}
//		participant.setPassword(incomingParticipant.getPassword());
//	   participantService.update(participant);
//	   return new ResponseEntity<Participant>(participant, HttpStatus.OK);
//	}
	

}
