package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.ScheduleTestDTO;
import com.fatec.labify.api.dto.ScheduledTestResponseDTO;
import com.fatec.labify.domain.*;
import com.fatec.labify.exception.NotFoundException;
import com.fatec.labify.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ScheduleTestService {

    private final UserService userService;
    private final PatientService patientService;
    private final BranchRepository branchRepository;
    private final PatientRepository patientRepository;
    private final TestRepository testRepository;
    private final ScheduleTestRepository scheduleTestRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final AccessControlService accessControlService;

    public ScheduleTestService(TestRepository testRepository, UserService userService, PatientService patientService, BranchRepository branchRepository, PatientRepository patientRepository, ScheduleTestRepository scheduleTestRepository, UserRepository userRepository, AuthenticationService authenticationService, AccessControlService accessControlService) {
        this.testRepository = testRepository;
        this.userService = userService;
        this.patientService = patientService;
        this.branchRepository = branchRepository;
        this.patientRepository = patientRepository;
        this.scheduleTestRepository = scheduleTestRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.accessControlService = accessControlService;
    }

    @Transactional(readOnly = true)
    public List<ScheduledTestResponseDTO> findAllCompletedTests(String username) {
        User user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("Usuário", username));

        String id = user.getId();

        return scheduleTestRepository.findByStatusAndPatient_Id(TestStatus.CONCLUIDO, id)
                .stream()
                .map(ScheduledTestResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduledTestResponseDTO> findAllScheduledTests(String username) {
        User user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("Usuário", username));

        String id = user.getId();
        return scheduleTestRepository.findByStatusAndPatient_Id(TestStatus.AGENDADO, id)
                .stream()
                .map(ScheduledTestResponseDTO::new)
                .toList();
    }

    @Transactional
    public ScheduledTestResponseDTO schedule(ScheduleTestDTO scheduleTestDTO, String username) {
        User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        authenticationService.validateVerified(user);

        Test test = testRepository.findById(scheduleTestDTO.getTestId()).orElseThrow(() -> new NotFoundException("Exame", scheduleTestDTO.getTestId()));
        Branch branch = branchRepository.findById(scheduleTestDTO.getBranchId()).orElseThrow(() -> new NotFoundException("Filial", scheduleTestDTO.getBranchId()));
        ScheduledTest scheduledTest = new ScheduledTest(UUID.randomUUID().toString(), test, user.getPatient(), TestStatus.AGENDADO, branch, scheduleTestDTO.getScheduledFor());
        scheduleTestRepository.save(scheduledTest);
        return new ScheduledTestResponseDTO(scheduledTest);
    }

    @Transactional
    public List<ScheduledTestResponseDTO> scheduleTests(List<ScheduleTestDTO> scheduledTests, String username) {
        User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        authenticationService.validateVerified(user);

        return scheduledTests.stream()
                .map(dto -> schedule(dto, username))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduledTestResponseDTO> findScheduledTestsByBranchId(String id, String username) {
        User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial", id));
        accessControlService.validateCanManageBranch(user, branch);
        return scheduleTestRepository.findByStatusAndBranch_Id(TestStatus.AGENDADO, id)
                .stream()
                .map(ScheduledTestResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduledTestResponseDTO> findCompletedTestsByBranchId(String id, String username) {
        User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial", id));
        accessControlService.validateCanManageBranch(user, branch);
        return scheduleTestRepository.findByStatusAndBranch_Id(TestStatus.CONCLUIDO, id)
                .stream()
                .map(ScheduledTestResponseDTO::new)
                .toList();
    }
}
