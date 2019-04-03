package springservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import springservice.domain.RegionsInfRepository;
import springservice.domain.RegionsRepository;

@RestController
public class UpdateController {
	@Autowired
	RegionsInfRepository regionsInfRepository;

	@Autowired
	RegionsRepository regionsRepository;
}
