package br.com.univali.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.univali.blog.models.User;
import br.com.univali.blog.services.UserService;
import br.com.univali.blog.services.ValidateService;

import javax.validation.Valid;

@Controller
public class RegistrationController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ValidateService validateService;
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {

		model.addAttribute("user", new User());
		return "/registration";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
		if (user.getUsername().isEmpty()) {
			bindingResult.rejectValue("username", "error.user", "Campo não pode ser vazio");
		}
		if (user.getPassword().isEmpty()) {
			bindingResult.rejectValue("password", "error.user", "Campo não pode ser vazio");
		}
		if (user.getEmail().isEmpty()) {
			bindingResult.rejectValue("email", "error.user", "Campo não pode ser vazio");
		}
		if (user.getName().isEmpty()) {
			bindingResult.rejectValue("name", "error.user", "Campo não pode ser vazio");
		}
		if (userService.findByEmail(user.getEmail()) != null) {
			bindingResult.rejectValue("email", "error.user", "Já existe um usuario com este email");
		}
		if (userService.findByUsername(user.getUsername()) != null) {
			bindingResult.rejectValue("username", "error.user", "Já existe um usuario com este username");
		}
		if (!validateService.validateStringNoEspecialCaracter(user.getUsername())) {
			bindingResult.rejectValue("username", "error.user", "Username não pode conter caracter especial");
		}	

		if (!bindingResult.hasErrors()) {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			user.setRole("USER");
			userService.save(user);

			model.addAttribute("successMessage", "Usuário registrado com sucesso");
			model.addAttribute("user", new User());
		}

		return "/registration";
	}
}
