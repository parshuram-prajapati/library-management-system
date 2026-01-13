package com.example.library_management.controller;

import com.example.library_management.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    // 🔔 MANUAL REMINDER API
    @GetMapping("/remind/{bookId}")
    public String sendReminder(@PathVariable String bookId) {
        return reminderService.sendManualReminder(bookId);
    }

    @PostMapping("/remind/{bookId}")
    public String sendReminderPost(@PathVariable String bookId) {
        return reminderService.sendManualReminder(bookId);
    }

    // Alternative endpoint path for reminders
    @GetMapping("/reminder/{bookId}")
    public String getReminderStatus(@PathVariable String bookId) {
        return reminderService.sendManualReminder(bookId);
    }

    @PostMapping("/reminder/{bookId}")
    public String postReminder(@PathVariable String bookId) {
        return reminderService.sendManualReminder(bookId);
    }
}
