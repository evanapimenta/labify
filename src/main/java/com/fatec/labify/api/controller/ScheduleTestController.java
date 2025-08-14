package com.fatec.labify.api.controller;

import com.fatec.labify.api.dto.ScheduleTestDTO;
import com.fatec.labify.api.dto.ScheduledTestResponseDTO;
import com.fatec.labify.api.service.ScheduleTestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleTestController {
    private final ScheduleTestService scheduleTestService;

    public ScheduleTestController(ScheduleTestService scheduleTestService) {
        this.scheduleTestService = scheduleTestService;
    }

    @PostMapping("/test")
    public ResponseEntity<ScheduledTestResponseDTO> scheduleTest(@AuthenticationPrincipal UserDetails userDetails, @Valid ScheduleTestDTO scheduleTestDTO) {
        return ResponseEntity.ok(scheduleTestService.schedule(scheduleTestDTO, userDetails.getUsername()));
    }

    @PostMapping()
    public ResponseEntity<List<ScheduledTestResponseDTO>> scheduleTests(@AuthenticationPrincipal UserDetails userDetails,
                                                                        @RequestBody List<ScheduleTestDTO> scheduleTest) {
        return ResponseEntity.ok(scheduleTestService.scheduleTests(scheduleTest, userDetails.getUsername()));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ScheduledTestResponseDTO>> getScheduledTests(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(scheduleTestService.findAllScheduledTests(userDetails.getUsername()));
    }

    @GetMapping("/me/completed")
    public ResponseEntity<List<ScheduledTestResponseDTO>> findCompletedTests(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(scheduleTestService.findAllCompletedTests(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ScheduledTestResponseDTO>> getScheduledTestsByBranchId(@AuthenticationPrincipal UserDetails userDetails,
                                                                                      @PathVariable String id) {
        return ResponseEntity.ok(scheduleTestService.findScheduledTestsByBranchId(id, userDetails.getUsername()));
    }

    // reschedule test
    // reschedule tests
    // cancel tests
    // cancel test
}
