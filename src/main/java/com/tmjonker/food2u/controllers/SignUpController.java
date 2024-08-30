package com.tmjonker.food2u.controllers;

import com.tmjonker.food2u.forms.NewUserForm;
import com.tmjonker.food2u.forms.ReturningUserForm;
import com.tmjonker.food2u.entities.user.User;
import com.tmjonker.food2u.logging.Food2uLogger;
import com.tmjonker.food2u.services.UserServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
public class SignUpController {

    private static final Logger LOGGER = Food2uLogger.setUp(SignUpController.class.getName());

    private UserServiceDetails userServiceDetails;

    @Autowired
    public SignUpController(UserServiceDetails userServiceDetails) {
        this.userServiceDetails = userServiceDetails;
    }

    @GetMapping("/sign-up")
    public String signUpForm(@ModelAttribute NewUserForm newUserForm) {

        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@ModelAttribute @Valid NewUserForm newUserForm, BindingResult bindingResult,
                               Model model, RedirectAttributes redirectAttributes) {

        /* validates input by checking to see if password1 matches password2. Also checks if the email entered is already
        associated with an existing user, and if all fields are adequately filled in.
         */
        if (!newUserForm.getPassword().equals(newUserForm.getPassword2())) { // if passwords don't match, don't process the form.
            newUserForm.setPasswordsMatch(false);
            return "sign-up";
        } else if (bindingResult.hasErrors()){ // if the form has errors as defined in template, do not process.
            return "sign-up";
        } else {
            User newUser = userServiceDetails.createNewUser(newUserForm);

            if (newUser != null) {

                // model attributes that are passed to the thymeleaf templates.
                model.addAttribute("user", newUser);
                return "welcome";
            } else { // if user already exists, redirect to sign in page.
                ReturningUserForm returningUserForm = new ReturningUserForm(newUserForm.getEmail(), true);
                redirectAttributes.addFlashAttribute("returningUserForm", returningUserForm);
                return "redirect:/sign-in";
            }
        }
    }
}