package com.carlosxmerca.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.carlosxmerca.model.dto.EmployeeDTO;
import com.carlosxmerca.model.entity.Employee;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
//@SessionAttributes("employee")
public class AuthController {
	
	private static final List<Employee> users = new ArrayList<>();
	private static Employee user = null;
	private static final boolean ACTIVE = true;
	private static final boolean INACTIVE = false;
	private static final boolean ADMIN = true;
	private static final boolean USER = false;
    
    static {
    	users.add(new Employee("Carlos", "Mercado", "CM123456", "12345", "31/12/2010", ADMIN, ACTIVE));
    	users.add(new Employee("Diego", "Rivas", "DR234567", "00000", "25/07/2018", USER, ACTIVE));
    	users.add(new Employee("Jose", "Acosta", "JA345678", "99999", "20/07/2018", USER, INACTIVE));
    	users.add(new Employee("Daniel", "Solis", "DS456789", "DS123","10/04/2023", ADMIN, ACTIVE));
    }
    
    @PostMapping("/auth")
	public String login(@ModelAttribute EmployeeDTO credentials) {
		Employee employee = users.stream()
				.filter(u -> u.getCode().equals(credentials.getCode()) && u.getPass().equals(credentials.getPass()))
				.findFirst()
				.orElse(null);


		if (employee == null || !employee.isActive())
			return "redirect:/404";
		
		try {
			Date currentDate = new Date();
			Date hireDate = new SimpleDateFormat("dd/MM/yyyy").parse(employee.getDateOfHire());
			if (hireDate.compareTo(currentDate) > 0) {
				return "redirect:/404";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		user = employee;
		
		return "redirect:/dashboard";
	}
    
    @GetMapping("/logout")
    public String logout() {
    	user = null;
    	return "redirect:/login";
    }
    
    @GetMapping("/dashboard")
	public String getDashboard(Model model) {
    	String time = Calendar.getInstance().getTime().toString();
    	model.addAttribute("time", time);
    	model.addAttribute("users", users);
    	
    	if (user == null)
    		return "redirect:/404";
    	    	
    	model.addAttribute("user", user.getNames());
    	model.addAttribute("isUserAdmin", user.isAdmin());
    	
    	return "dashboard";
    }
	
/*
	@GetMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		Employee employee = (Employee) session.getAttribute("employee");
		if (employee != null) {
			model.addAttribute("employee", employee);
			String time = Calendar.getInstance().getTime().toString();
    		model.addAttribute("time", time);
			System.out.println(employee);
			return "dashboard";
		} else {
			return "redirect:/login";
		}
    }
*/
        
	@GetMapping("/login")
    public String getLogin() {
    	return "login";
    }
	
	@GetMapping("/404")
    public String get404() {
    	return "not-found";
    }
	
}
