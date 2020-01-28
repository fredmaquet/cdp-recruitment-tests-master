package adeo.leroymerlin.cdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import adeo.leroymerlin.cdp.pojo.Event;
import adeo.leroymerlin.cdp.repository.EventRepository;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    public void delete(Long id) {
        eventRepository.delete(id);
    }
    
	public Event updateEvent(Long id, Event event) {
		// In order to avoid a lot of access on database
		// by doing eventRepository.save(event) 
		// we just update nbStars and comment because all bands are members
		// are not supposed to be updated for the moment
		Event eventFromRepository = eventRepository.findOne(id);
		eventFromRepository.setNbStars(event.getNbStars());
		eventFromRepository.setComment(event.getComment());
		return eventRepository.save(eventFromRepository);
	}

    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here

        return events;
    }
}
