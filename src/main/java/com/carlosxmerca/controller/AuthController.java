package com.carlosxmerca.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.carlosxmerca.model.dto.EmployeeDTO;
import com.carlosxmerca.model.entity.Employee;

@Controller
public class AuthController {
	
	private static List<Employee> users = new ArrayList<>();
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
				.filter(u -> u.getCode().equals(credentials.getCode())).findFirst().orElse(null);
		
		if (employee == null)
			return "redirect:/404";
		if (!employee.isActive())
			return "redirect:/404";
		
		try {
			Date hireDate = new SimpleDateFormat("dd/MM/yyyy").parse(employee.getDateOfHire());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
			
		return "redirect:/dashboard";
	}
	
	@GetMapping("/dashboard")
    public String getDashboard(Model model) {
		String time = Calendar.getInstance().getTime().toString();
    	model.addAttribute("time", time);
    	
    	return "dashboard";
    }
	
	@GetMapping("/login")
    public String getLogin() {
    	return "login";
    }
	
}
