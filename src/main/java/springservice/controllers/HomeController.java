package springservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import springservice.domain.PostsRepository;
import springservice.domain.PostsSaveRequestDto;

@RestController
@AllArgsConstructor
public class HomeController {

	private PostsRepository postsRepository;

	@RequestMapping("/")
	public String home() {
		return "안녕 Spring Boot!";
	}

	@PostMapping("/posts")
	public void savePosts(@RequestBody PostsSaveRequestDto dto) {
		//System.out.println(dto.getTitle()+"!!");
		postsRepository.save(dto.toEntity());
	}
}
