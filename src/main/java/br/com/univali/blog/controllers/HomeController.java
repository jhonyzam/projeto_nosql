package br.com.univali.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping({"/", "/post/new"})
	public String redirToList() {
		return "redirect:/blog/list";
	}
}
