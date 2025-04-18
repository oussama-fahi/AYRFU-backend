package com.uddan.ayrfu.controller;

import com.uddan.ayrfu.dto.PositionDTO;
import com.uddan.ayrfu.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping("/public/positions")
    public ResponseEntity<List<PositionDTO>> getAllActivePositions() {
        List<PositionDTO> positions = positionService.getAllActivePositions();
        return ResponseEntity.ok(positions);
    }

    @PostMapping("/public/positions/match")
    public ResponseEntity<List<PositionDTO>> getMatchedPositions(@RequestBody Map<String, String> preferences) {
        List<PositionDTO> matchedPositions = positionService.getMatchedPositions(preferences);
        return ResponseEntity.ok(matchedPositions);
    }

    @PostMapping("/admin/positions")
    public ResponseEntity<PositionDTO> createPosition(@RequestBody PositionDTO positionDTO) {
        PositionDTO createdPosition = positionService.createPosition(positionDTO);
        return ResponseEntity.ok(createdPosition);
    }

    @PutMapping("/admin/positions/{id}")
    public ResponseEntity<PositionDTO> updatePosition(@PathVariable Long id, @RequestBody PositionDTO positionDTO) {
        PositionDTO updatedPosition = positionService.updatePosition(id, positionDTO);
        return ResponseEntity.ok(updatedPosition);
    }

    @DeleteMapping("/admin/positions/{id}")
    public ResponseEntity<?> deactivatePosition(@PathVariable Long id) {
        positionService.deactivatePosition(id);
        return ResponseEntity.ok().build();
    }
}