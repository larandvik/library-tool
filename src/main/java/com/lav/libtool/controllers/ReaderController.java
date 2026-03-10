package com.lav.libtool.controllers;

import com.lav.libtool.dto.reader.ReaderCreateRequestDTO;
import com.lav.libtool.dto.reader.ReaderResponseDTO;
import com.lav.libtool.dto.reader.ReaderUpdateRequestDTO;
import com.lav.libtool.service.ReaderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/readers")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService service;

    @GetMapping
    public ResponseEntity<List<ReaderResponseDTO>> getAll(){
        log.info("GET request: fetch all readers");
        var response = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderResponseDTO> getById(@PathVariable Long id){
        log.info("GET request: fetch reader by id {}", id);
        var response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReaderResponseDTO> create(@Valid @RequestBody ReaderCreateRequestDTO request) {
        log.info("POST request: create reader");
        var response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReaderResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ReaderUpdateRequestDTO request) {
        log.info("PUT request: update reader with id {}", id);
        var response = service.update(id, request);
        return  ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        log.info("DELETE request: delete reader with id {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
