package com.movie_admin.controller;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.movie_admin.service.MemberService;
import com.movie_admin.vo.MemberBean;

@Controller
public class HomeController {
	
	@Inject
	MemberService memberService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		List<MemberBean> list = memberService.getMember();
		model.addAttribute("memberList", list);
		return "home";
	}
	
	
	
}