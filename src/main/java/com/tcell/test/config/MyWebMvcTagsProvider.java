package com.tcell.test.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTags;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcTagsProvider;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

@Component
public class MyWebMvcTagsProvider implements WebMvcTagsProvider {

	@Override
	public Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response, Object handler,
			Throwable exception) {
		//System.out.println(request.getRequestURI());
		StringBuilder uri = new StringBuilder();
		uri.append(request.getRequestURI());
		if (request.getQueryString() != null) {
			uri.append("?");
			uri.append(request.getQueryString());
		}
		
		Tags tags = Tags.of(WebMvcTags.method(request), Tag.of("uri", uri.toString()),
				WebMvcTags.exception(exception), WebMvcTags.status(response),
				WebMvcTags.outcome(response));
		return tags;
	}

	@Override
	public Iterable<Tag> getLongRequestTags(HttpServletRequest request, Object handler) {
		return Tags.of(WebMvcTags.method(request), WebMvcTags.uri(request, null));
	}

}
