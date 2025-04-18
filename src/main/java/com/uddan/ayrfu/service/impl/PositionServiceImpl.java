package com.uddan.ayrfu.service.impl;

import com.uddan.ayrfu.service.MatchingService;
import com.uddan.ayrfu.service.PositionService;
import com.uddan.ayrfu.dto.PositionDTO;
import com.uddan.ayrfu.model.Position;
import com.uddan.ayrfu.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private MatchingService matchingService;

    public List<PositionDTO> getAllActivePositions() {
        return positionRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PositionDTO> getMatchedPositions(Map<String, String> preferences) {
        List<Position> allPositions = positionRepository.findByIsActiveTrue();

        return allPositions.stream()
                .map(position -> {
                    PositionDTO dto = convertToDTO(position);
                    dto.setMatchScore(matchingService.calculatePositionMatchScore(position, preferences));
                    return dto;
                })
                .filter(dto -> dto.getMatchScore() > 0.3) // Minimum match threshold
                .sorted((p1, p2) -> Double.compare(p2.getMatchScore(), p1.getMatchScore()))
                .collect(Collectors.toList());
    }

    public PositionDTO createPosition(PositionDTO positionDTO) {
        Position position = convertToEntity(positionDTO);
        position.setCreatedAt(LocalDateTime.now());
        position.setUpdatedAt(LocalDateTime.now());
        position.setIsActive(true);

        Position savedPosition = positionRepository.save(position);
        return convertToDTO(savedPosition);
    }

    public PositionDTO updatePosition(Long id, PositionDTO positionDTO) {
        Position existingPosition = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        existingPosition.setTitle(positionDTO.getTitle());
        existingPosition.setDescription(positionDTO.getDescription());
        existingPosition.setTechnology(positionDTO.getTechnology());
        existingPosition.setLocation(positionDTO.getLocation());
        existingPosition.setLanguages(positionDTO.getLanguages());
        existingPosition.setExperienceLevel(positionDTO.getExperienceLevel());
        existingPosition.setWorkModel(positionDTO.getWorkModel());
        existingPosition.setUpdatedAt(LocalDateTime.now());

        Position updatedPosition = positionRepository.save(existingPosition);
        return convertToDTO(updatedPosition);
    }

    public void deactivatePosition(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        position.setIsActive(false);
        position.setUpdatedAt(LocalDateTime.now());
        positionRepository.save(position);
    }

    private PositionDTO convertToDTO(Position position) {
        PositionDTO dto = new PositionDTO();
        dto.setId(position.getId());
        dto.setTitle(position.getTitle());
        dto.setDescription(position.getDescription());
        dto.setTechnology(position.getTechnology());
        dto.setLocation(position.getLocation());
        dto.setLanguages(position.getLanguages());
        dto.setExperienceLevel(position.getExperienceLevel());
        dto.setWorkModel(position.getWorkModel());
        dto.setIsActive(position.getIsActive());
        return dto;
    }

    private Position convertToEntity(PositionDTO dto) {
        Position position = new Position();
        position.setId(dto.getId());
        position.setTitle(dto.getTitle());
        position.setDescription(dto.getDescription());
        position.setTechnology(dto.getTechnology());
        position.setLocation(dto.getLocation());
        position.setLanguages(dto.getLanguages());
        position.setExperienceLevel(dto.getExperienceLevel());
        position.setWorkModel(dto.getWorkModel());
        position.setIsActive(dto.getIsActive());
        return position;
    }
}
