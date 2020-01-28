package adeo.leroymerlin.cdp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import adeo.leroymerlin.cdp.pojo.Event;
import adeo.leroymerlin.cdp.service.EventService;


@RunWith(SpringRunner.class)
public class EventControllerTest {

    @Mock
    private EventService eventService;
    
    @InjectMocks
    private EventController eventController;
    
    private MockMvc mockMvc;
    private ObjectMapper mapper;
    
	private static final Long EVENT_ID = 1L;
	
    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).alwaysDo(print()).build();
        mapper = new ObjectMapper();
    }
    
    @Test
    public void should_getEvents() throws Exception {
        
		Event event1 = new Event();
		event1.setTitle("GrasPop Metal Meeting");
		
		Event event2 = new Event();
		event2.setTitle("Alcatraz Fest");

		List<Event> eventsFromService =  Arrays.asList(event1,event2);
    	
    	// Given the service returns all events
        when(eventService.getEvents()).thenReturn(eventsFromService);

        // When we call the controller to get all events
        ResultActions result = mockMvc.perform(get("/api/events/")
                .contentType(MediaType.APPLICATION_JSON));
        
        // Then we check the status 200 and the returned values are the same as the service
        result.andExpect(status().isOk()).andExpect(content().string(mapper.writeValueAsString(eventsFromService)));      
        
		// And the service has been called only once
        verify(eventService,times(1)).getEvents();
		verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void should_deleteEvent() throws Exception {
        // When we call the controller to delete an event
        ResultActions result = mockMvc.perform(delete("/api/events/" + EVENT_ID)
                .contentType(MediaType.APPLICATION_JSON));
        
        // Then we check the status 200
        result.andExpect(status().isOk());      
        
		// And the service has been called only once
        verify(eventService,times(1)).delete(EVENT_ID);
		verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void should_updateEvent() throws Exception {
    	//Given an event to update
    	Event eventToUpdate = mapper.readValue(new File("src/test/resources/json/updateEvent.json"),Event.class);
    	
    	// We put an argumentCaptor in order to see what it should be passed on service
    	ArgumentCaptor<Event> captorOfEvent = ArgumentCaptor.forClass(Event.class);
    	
    	Event eventFromService = new Event();
		eventFromService.setTitle("GrasPop Metal Meeting");
		eventFromService.setComment("adding new comment on that amazing event");
		eventFromService.setNbStars(5);
		
		// Given the service returns the updated event
        when(eventService.updateEvent(eq(EVENT_ID), captorOfEvent.capture())).thenReturn(eventFromService);
        
        // When we call the controller to update event
        ResultActions result = mockMvc.perform(put("/api/events/"+ EVENT_ID)
				.content(mapper.writeValueAsString(eventToUpdate))
				.contentType(MediaType.APPLICATION_JSON));
        
        
		// Then we check the status 200 and the returned values are the same as the service
		result.andExpect(status().isOk()).andExpect(content().string(mapper.writeValueAsString(eventFromService)));

		// And we check the event send to the service
		Event eventSendedToService = captorOfEvent.getValue();
		assertThat(eventSendedToService).isEqualToComparingFieldByFieldRecursively(eventToUpdate);

		// And the service has been called only once
		verify(eventService, times(1)).updateEvent(EVENT_ID, eventSendedToService);
		verifyNoMoreInteractions(eventService);
    }
    
    @Test
    public void should_findEvents() throws Exception {
    	Event motocultor = new Event();
		motocultor.setTitle("Motocultor");
		
		Event downloadFestival =  new Event();
		downloadFestival.setTitle("Download Festival");
		
		List<Event> listFilteredEvent = Arrays.asList(motocultor,downloadFestival);
		
		// Given the service returns the filtered list of event
        when(eventService.getFilteredEvents("Wa")).thenReturn(listFilteredEvent );

        // When we call the controller to filter events
        ResultActions result = mockMvc.perform(get("/api/events/search/Wa")
                .contentType(MediaType.APPLICATION_JSON));
        
        // Then we check the status 200 and the returned values are the same as the service
        result.andExpect(status().isOk()).andExpect(content().string(mapper.writeValueAsString(listFilteredEvent)));      
        
        // And the service has been called only once
        verify(eventService,times(1)).getFilteredEvents("Wa");
		verifyNoMoreInteractions(eventService);
    }
    
}
