package adeo.leroymerlin.cdp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import adeo.leroymerlin.cdp.pojo.Event;
import adeo.leroymerlin.cdp.repository.EventRepository;

@RunWith(SpringRunner.class)
public class EventServiceTest {

	@Mock
	private EventRepository eventRepository;

	@InjectMocks
	private EventService eventService;
	
	private static final Long EVENT_ID = 1L;

	@Before
	public void init() {
	}

	@Test
	public void should_getEvents() {

		Event event1 = new Event();
		event1.setTitle("GrasPop Metal Meeting");

		Event event2 = new Event();
		event2.setTitle("Alcatraz Fest");

		// Given a list of events on repository
		List<Event> eventsFromRepository = Arrays.asList(event1, event2);
		when(eventRepository.findAllBy()).thenReturn(eventsFromRepository);

		// When we call the service to get all event
		List<Event> result = eventService.getEvents();

		// Then we check the list of events is the same as the repository
		assertThat(result).isNotNull().isEqualTo(eventsFromRepository);

		// And the repository has been called only once
		verify(eventRepository, times(1)).findAllBy();
		verifyNoMoreInteractions(eventRepository);

	}

	@Test
	public void should_deleteEvent() {
		// When we delete an event
		eventService.delete(EVENT_ID);

		// The we check the repository has been called once
		verify(eventRepository, times(1)).delete(EVENT_ID);
		verifyNoMoreInteractions(eventRepository);
	}

}
