package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.GeolocationDTO;
import com.fatec.labify.api.dto.branch.CreateBranchDTO;
import com.fatec.labify.api.dto.branch.CreateBranchResponseDTO;
import com.fatec.labify.api.dto.branch.UpdateBranchDTO;
import com.fatec.labify.api.dto.branch.BranchResponseDTO;
import com.fatec.labify.client.NominatimClient;
import com.fatec.labify.domain.*;
import com.fatec.labify.exception.NotFoundException;
import com.fatec.labify.repository.BranchRepository;
import com.fatec.labify.repository.LaboratoryRepository;
import com.fatec.labify.repository.UserRepository;
import com.fatec.labify.util.AddressUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BranchService {

    private final AccessControlService accessControlService;
    private final NominatimClient nominatimClient;
    private final BranchRepository branchRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final UserRepository userRepository;

    public BranchService(BranchRepository branchRepository,
                         LaboratoryRepository laboratoryRepository,
                         UserRepository userRepository,
                         AccessControlService accessControlService,
                         NominatimClient nominatimClient) {
        this.branchRepository = branchRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.userRepository = userRepository;
        this.accessControlService = accessControlService;
        this.nominatimClient = nominatimClient;
    }

    public BranchResponseDTO findById(String username, String id) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial", id));
        accessControlService.canManageBranch(user, branch);
        return new BranchResponseDTO(branch);
    }

    public Page<BranchResponseDTO> findByLabId(String username, String id, Pageable pageable) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Laboratory lab = laboratoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Laboratório", id));
        accessControlService.canManageLab(user, lab);
        return branchRepository.findBranchByLaboratory_Id(id, pageable).map(BranchResponseDTO::new);
    }

    public Page<BranchResponseDTO> getClosestBranches(String username, double frontLat, double frontLon, int limit) {
        User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));

        double userLat = user.getPatient().getAddress().getLatitude();
        double userLon = user.getPatient().getAddress().getLongitude();

        double searchLat = isSameLocation(frontLat, frontLon, userLat, userLon) ? userLat : frontLat;
        double searchLon = isSameLocation(frontLat, frontLon, userLat, userLon) ? userLon : frontLon;

        PageRequest pageable = PageRequest.of(0, limit);

        Page<Object[]> results = branchRepository.findClosestBranchesWithDistance(searchLat, searchLon, pageable);

        Page<BranchResponseDTO> dtoPage = results.map(row -> {
            BranchResponseDTO dto = new BranchResponseDTO();
            dto.setId((String) row[0]);
            dto.setName((String) row[1]);
            dto.setPhoneNumber((String) row[2]);
            dto.setEmail((String) row[3]);
            dto.setOpeningHours((String) row[4]);
            dto.setCreatedAt(((java.sql.Timestamp) row[5]).toLocalDateTime());
            dto.setUpdatedAt(((java.sql.Timestamp) row[6]).toLocalDateTime());

            dto.setAddress(AddressUtils.fromRow(row, 7));
            dto.setDistanceKm(Math.round(((Number) row[15]).doubleValue() * 100.0) / 100.0);

            return dto;
        });


        return dtoPage;
    }

    @Transactional
    public CreateBranchResponseDTO create(CreateBranchDTO dto) {
        Laboratory lab = laboratoryRepository.findById(dto.getLaboratoryId()).orElseThrow(() -> new NotFoundException("Laboratório", dto.getLaboratoryId()));

        GeolocationDTO geolocationDTO = nominatimClient.getGeolocation(dto.getAddressDTO());
        Address address = AddressUtils.setAddress(dto.getAddressDTO(), geolocationDTO);

        Branch branch = new Branch(
                dto.getName(),
                dto.getEmail(),
                dto.getPhoneNumber(),
                dto.getOpeningHours(),
                lab,
                address);

        branchRepository.save(branch);
        return new CreateBranchResponseDTO(branch);
    }

    @Transactional
    public BranchResponseDTO update(String username, String id, UpdateBranchDTO dto) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial", id));
        accessControlService.validateCanManageBranch(user, branch);

        Optional.ofNullable(dto.getName()).ifPresent(branch::setName);
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(branch::setPhoneNumber);
        Optional.ofNullable(dto.getEmail()).ifPresent(branch::setEmail);
        Optional.ofNullable(dto.getOpeningHours()).ifPresent(branch::setOpeningHours);

        Optional.ofNullable(dto.getAddressDTO()).ifPresent(addressDTO -> {
            Address address = AddressUtils.updateAddress(branch.getAddress(), addressDTO);
            branch.setAddress(address);
        });

        branchRepository.save(branch);
        return new BranchResponseDTO(branch);
    }

    @Transactional
    public void activate(String username, String id) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial", id));
        accessControlService.validateCanManageLab(user, branch.getLaboratory());
        branch.activate();
        branchRepository.save(branch);
    }

    @Transactional
    public void delete(String id, String username) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Laboratório", id));
        accessControlService.validateCanManageLab(user, branch.getLaboratory());
        branch.deactivate();
        branchRepository.save(branch);
    }

    private boolean isSameLocation(double frontLat, double frontLon, double userLat, double userLon) {
        return Double.compare(frontLat, userLat) == 0 && Double.compare(frontLon, userLon) == 0;
    }

}
