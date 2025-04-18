package com.uddan.ayrfu.controller;

@RestController
@RequestMapping("/api")
public class ServiceController {

    @Autowired
    private ServiceEntityService serviceEntityService;

    @GetMapping("/public/services")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServiceDTO> services = serviceEntityService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @PostMapping("/public/services/match")
    public ResponseEntity<List<ServiceDTO>> getMatchedServices(@RequestBody Map<String, String> query) {
        List<ServiceDTO> matchedServices = serviceEntityService.getMatchedServices(query.get("query"));
        return ResponseEntity.ok(matchedServices);
    }

    @PostMapping("/admin/services")
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
        ServiceDTO createdService = serviceEntityService.createService(serviceDTO);
        return ResponseEntity.ok(createdService);
    }

    @PutMapping("/admin/services/{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable Long id, @RequestBody ServiceDTO serviceDTO) {
        ServiceDTO updatedService = serviceEntityService.updateService(id, serviceDTO);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/admin/services/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        serviceEntityService.deleteService(id);
        return ResponseEntity.ok().build();
    }
}