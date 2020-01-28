package adeo.leroymerlin.cdp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import adeo.leroymerlin.cdp.pojo.Band;
import adeo.leroymerlin.cdp.pojo.Event;
import adeo.leroymerlin.cdp.pojo.Member;
import adeo.leroymerlin.cdp.repository.EventRepository;

@RunWith(SpringRunner.class)
public class EventServiceTest {

	@Mock
	private EventRepository eventRepository;

	@InjectMocks
	private EventService eventService;
	
	private static final Long EVENT_ID = 1L;
	
	private Band sum41;
	private Band pinkFloyd;
	private Event motocultor;
	private Event downloadFestival;
	private Event noBandFestival;
	
	@Before
	public void init() {
		///// CREATE BAND sum41
		Member member1Sum41 = new Member();
		member1Sum41.setName("Queen Charlie Wolf (Chick)");
		Member member2Sum41 = new Member();
		member2Sum41.setName("Queen Jamie Petty");
		Member member3Sum41 = new Member();
		member3Sum41.setName("Queen Danielle Connor (Dannon)");
		sum41 = new Band();
		sum41.setName("Sum41");
		sum41.setMembers(new HashSet<>(Arrays.asList(member1Sum41, member2Sum41, member3Sum41)));

		////// CREATE BAND pinkFloyd
		Member member1pinkFloyd = new Member();
		member1pinkFloyd.setName("Queen Frankie Gross (Fania)");
		Member member2pinkFloyd = new Member();
		member2pinkFloyd.setName("Queen Fred Wolf (Chick)");
		pinkFloyd = new Band();
		pinkFloyd.setName("Pink flyod");
		pinkFloyd.setMembers(new HashSet<>(Arrays.asList(member1pinkFloyd, member2pinkFloyd)));

		////// CREATE EVENT Motocultor with band Sum41
		motocultor = new Event();
		motocultor.setTitle("Motocultor");
		motocultor.setBands(new HashSet<>(Arrays.asList(sum41)));

		/////// CREATE EVENT Download Festival with band sum41 and pink floyd
		downloadFestival = new Event();
		downloadFestival.setTitle("Download Festival");
		downloadFestival.setBands(new HashSet<>(Arrays.asList(sum41, pinkFloyd)));

		// CREATE EVENT noBandFestival with no band
		noBandFestival = new Event();
		noBandFestival.setTitle("No band festival");
		noBandFestival.setBands(new HashSet<>());
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
	
	@Test
	public void should_update_event() {
		// Given an event present on the repository
		Event eventFromRepository = new Event();
		eventFromRepository.setId(EVENT_ID);
		eventFromRepository.setTitle("Les Vieilles Charrues");
		when(eventRepository.findOne(EVENT_ID)).thenReturn(eventFromRepository);
		
		// And a event to update (nbStars and comment)
		Event eventUpdated = new Event();
		eventUpdated.setId(EVENT_ID);
		eventUpdated.setTitle("Les Vieilles Charrues");
		eventUpdated.setNbStars(5);
		eventUpdated.setComment("Amazing event !!!!");
		
		// We put an argumentCaptor in order to see what it should be saved on repository
		ArgumentCaptor<Event> captorOfEvent = ArgumentCaptor.forClass(Event.class);

		// And the repository save the event
		when(eventRepository.save(captorOfEvent.capture())).thenReturn(eventUpdated);
		
		// When we update the event
		Event result = eventService.updateEvent(EVENT_ID, eventUpdated);
		
		// Then we check the result equals the updatedEvent
		assertThat(result).isNotNull().isEqualTo(eventUpdated);
		
		// And we check all attributes of event updated (nbStars and comment)
		Event eventSavedOnRepository = captorOfEvent.getValue();
		assertThat(eventSavedOnRepository.getNbStars()).isEqualTo(5);
		assertThat(eventSavedOnRepository.getComment()).isEqualTo("Amazing event !!!!");

		// And the repository has been called only twice (findOne/save)
		verify(eventRepository, times(1)).findOne(EVENT_ID);
		verify(eventRepository, times(1)).save(eventSavedOnRepository);
		verifyNoMoreInteractions(eventRepository);
	}

	@Test
	public void should_filter_event_with_no_result() {
		// Given the repository returns all events
		when(eventRepository.findAllBy()).thenReturn(Arrays.asList(motocultor,downloadFestival,noBandFestival));

		// When we filtered all events by unknown member name
		List<Event> noResult = eventService.getFilteredEvents("NO MATCHING PATTERN");
		
		// Then we check the result is empty
		assertThat(noResult).isEmpty();
		
		// And the repository has been called once
		verify(eventRepository,times(1)).findAllBy();
		verifyNoMoreInteractions(eventRepository);
	}
	
	@Test
	public void should_filter_event_with_pinkfloyd_member() {
		// Given the repository returns all events
		when(eventRepository.findAllBy()).thenReturn(Arrays.asList(motocultor,downloadFestival,noBandFestival));

		// When we filter events by a known member name (belong to sum41 member)
		List<Event> eventsFiltered = eventService.getFilteredEvents("Wolf");

		// Then we check the result has two elements(motocultor (sum41) AND downloadfestival (sum41 and pinkfloyd))		
		assertThat(eventsFiltered).isNotEmpty().hasSize(2);
		
		// MOTOCULTOR : checking event
		Event motocultor_event = eventsFiltered.get(0);
		assertThat(motocultor_event.getTitle()).isEqualTo("Motocultor [1]");
		
		// MOTOCULTOR : checking bands
		assertThat(motocultor_event.getBands()).isNotEmpty().hasSize(1);
		List<Band> motocultor_bands = new ArrayList<>(motocultor_event.getBands());
		assertThat(motocultor_bands.get(0).getName()).isEqualTo("Sum41 [1]");
		
		// MOTOCULTOR : Checking member of the band
		List<Member> sum41_members_motocultor = new ArrayList<>(motocultor_bands.get(0).getMembers());
		assertThat(sum41_members_motocultor).isNotEmpty().hasSize(1);
		assertThat(sum41_members_motocultor.get(0).getName()).isEqualTo("Queen Charlie Wolf (Chick)");
		
		// DOWNLOAD FESTIVAL : checking event
		Event downloadFestival_event = eventsFiltered.get(1);
		assertThat(downloadFestival_event.getTitle()).isEqualTo("Download Festival [2]");
		
		// DOWNLOAD FESTIVAL : checking bands
		assertThat(downloadFestival_event.getBands()).isNotEmpty().hasSize(2);
		List<Band> downloadFestival_bands = new ArrayList<>(downloadFestival_event.getBands());
		assertThat(downloadFestival_bands.get(0).getName()).isEqualTo("Sum41 [1]");
		assertThat(downloadFestival_bands.get(1).getName()).isEqualTo("Pink flyod [1]");
		
		// DOWNLOAD FESTIVAL : Checking member of the band (sum41)
		List<Member> sum41_members_downloadFestival= new ArrayList<>(downloadFestival_bands.get(0).getMembers());
		assertThat(sum41_members_downloadFestival).isNotEmpty().hasSize(1);
		assertThat(sum41_members_downloadFestival.get(0).getName()).isEqualTo("Queen Charlie Wolf (Chick)");
		
		// DOWNLOAD FESTIVAL : Checking member of the band (pinkFlyod)
		List<Member> pinkFlyod_members_downloadFestival= new ArrayList<>(downloadFestival_bands.get(1).getMembers());
		assertThat(pinkFlyod_members_downloadFestival).isNotEmpty().hasSize(1);
		assertThat(pinkFlyod_members_downloadFestival.get(0).getName()).isEqualTo("Queen Fred Wolf (Chick)");
		
		// And the repository has been called once
		verify(eventRepository,times(1)).findAllBy();
		verifyNoMoreInteractions(eventRepository);
		
	}
	
}
