package com.tcell.test.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tcell.test.exception.NotFoundException;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@ControllerAdvice
public class ExceptionController {
	MeterRegistry registry;
	Counter notfound;
	public ExceptionController(MeterRegistry registry) {
		this.registry = registry;
		
		notfound = registry.counter("notfound");
	}
	@ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @Timed
    public boolean handleNotFoundException(NotFoundException ex) {
		notfound.increment();
        return false;
    }
}
