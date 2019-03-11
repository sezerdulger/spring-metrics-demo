package com.tcell.test.filter;

import org.springframework.boot.actuate.endpoint.EndpointFilter;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;

public class MyEndpointFilter implements EndpointFilter<ExposableEndpoint<?>>{

	@Override
	public boolean match(ExposableEndpoint<?> endpoint) {
		return true;
	}

}
