package adeo.leroymerlin.cdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import adeo.leroymerlin.cdp.pojo.Band;
import adeo.leroymerlin.cdp.pojo.Event;
import adeo.leroymerlin.cdp.pojo.Member;
import adeo.leroymerlin.cdp.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
		return events.stream()
				.map(event -> {
					List<Band> bandsFiltres = event.getBands().stream()
							.map(band -> {
								List<Member> members = band.getMembers().stream()
										.filter(member -> member.getName().contains(query))
										.collect(Collectors.toList());
									band.setMembers(new HashSet<Member>(members));
									Band newBand = new Band();
									newBand.setMembers(band.getMembers());
									newBand.setName(band.getName() + " [" + band.getMembers().size()+"]");
									return newBand;
							})
							.filter(band -> !band.getMembers().isEmpty())
							.collect(Collectors.toList());
					event.setBands(new HashSet<Band>(bandsFiltres));
					
					long nbChilds = event.getBands().stream()
					.flatMap(band -> band.getMembers().stream()).count();
					event.setTitle(event.getTitle() + " [" + nbChilds+"]");
					return event;
				})
				.filter(event -> !event.getBands().isEmpty())
				.collect(Collectors.toList());
	}
}
