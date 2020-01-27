package adeo.leroymerlin.cdp.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import adeo.leroymerlin.cdp.pojo.Event;

import java.util.List;

@Transactional
public interface EventRepository extends Repository<Event, Long> {

    void delete(Long eventId);

    List<Event> findAllBy();
}
