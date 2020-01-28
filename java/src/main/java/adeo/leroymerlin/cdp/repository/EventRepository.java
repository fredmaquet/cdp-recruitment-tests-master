package adeo.leroymerlin.cdp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import adeo.leroymerlin.cdp.pojo.Event;

@Transactional
public interface EventRepository extends CrudRepository<Event, Long> {

    void delete(Long eventId);

    List<Event> findAllBy();
}
