package com.tcell.test.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcell.test.exception.NotFoundException;
import com.tcell.test.model.Person;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;


@RestController
public class HomeController {
	List<Person> people = new ArrayList<Person>();
	Counter steveCounter;
	Counter personCounter;
	Timer findPersonTimer;
	Map<String, Counter> qCounters = new HashMap<>();
	
	MeterRegistry registry;

	public HomeController(MeterRegistry registry) {
		// registers a gauge to observe the size of the population
		//registry..collectionSize("population", Collections.emptyList(), people);
		this.registry = registry;
		// register a counter of questionable usefulness
		steveCounter = registry.counter("find_steve" /* optional tags here */);
		

		// register a timer -- though for request timing it is easier to use @Timed
		findPersonTimer = registry.timer("http_requests_for_person", "method", "GET");
	}

	@GetMapping("/api/person")
	public Person findPerson(@RequestParam String q) throws NotFoundException {
		if (q.equals("sezer")) {
			throw new NotFoundException("not found");
		}
		
		if(qCounters.containsKey(q)) {
			qCounters.get(q).increment();
		}
		else {
			qCounters.put(q, registry.counter(q));
		}
		
		return findPersonTimer.record(() -> { // use the timer!
			if(q.toLowerCase().contains("steve")) {
				steveCounter.increment(); // use the counter
			}

			return people.stream().filter(p -> p.name.equals(q)).findAny().orElse(null);
		});
	}
}
