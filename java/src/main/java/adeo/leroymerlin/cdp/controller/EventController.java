package adeo.leroymerlin.cdp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import adeo.leroymerlin.cdp.pojo.Event;
import adeo.leroymerlin.cdp.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping(value = "/")
    public List<Event> findEvents() {
        return eventService.getEvents();
    }

    @GetMapping(value = "/search/{query}")
    public List<Event> findEvents(@PathVariable String query) {
        return eventService.getFilteredEvents(query);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
    }

    @PutMapping(value = "/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
    	return eventService.updateEvent(id, event);
    }
}
