package com.challengers.challengeservice.photocheck.controller;

import com.challengers.challengeservice.photocheck.dto.CheckRequest;
import com.challengers.challengeservice.photocheck.dto.PhotoCheckRequest;
import com.challengers.challengeservice.photocheck.dto.PhotoCheckResponse;
import com.challengers.challengeservice.photocheck.service.PhotoCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/photo_check")
@RequiredArgsConstructor
public class PhotoCheckController {
    private final PhotoCheckService photoCheckService;

    @GetMapping("/{photo_check_id}")
    public ResponseEntity<PhotoCheckResponse> getPhotoCheck(@PathVariable(name = "photo_check_id")Long photoCheckId) {
        return ResponseEntity.ok(photoCheckService.findPhotoCheck(photoCheckId));
    }

    @PostMapping
    public ResponseEntity<Void> addPhotoCheck(@ModelAttribute PhotoCheckRequest photoCheckRequest,
                                              @RequestHeader(value = "userId") String userId) {
        Long photoCheckId = photoCheckService.addPhotoCheck(photoCheckRequest, Long.parseLong(userId));
        return ResponseEntity.created(URI.create("/api/photo_check/"+photoCheckId)).build();
    }

    @PostMapping("/pass")
    public ResponseEntity<Void> pass(@RequestBody CheckRequest checkRequest,
                                     @RequestHeader(value = "userId") String userId) {
        photoCheckService.passPhotoCheck(checkRequest, Long.parseLong(userId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/fail")
    public ResponseEntity<Void> fail(@RequestBody CheckRequest checkRequest,
                                     @RequestHeader(value = "userId") String userId) {
        photoCheckService.failPhotoCheck(checkRequest, Long.parseLong(userId));
        return ResponseEntity.ok().build();
    }

}
