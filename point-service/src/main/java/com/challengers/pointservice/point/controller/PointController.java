package com.challengers.pointservice.point.controller;


import com.challengers.pointservice.point.dto.PointTransactionResponse;
import com.challengers.pointservice.point.dto.PointResponse;
import com.challengers.pointservice.point.dto.PointUpdateRequest;
import com.challengers.pointservice.point.global.dto.GiveRewardDto;
import com.challengers.pointservice.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {
    private final PointService pointService;

    @GetMapping
    public ResponseEntity<PointResponse> getMyPoint(@RequestHeader(name = "userId") Long userId) {
        return ResponseEntity.ok(pointService.getMyPoint(userId));
    }

    @GetMapping("/transaction")
    public ResponseEntity<Page<PointTransactionResponse>> getMyPointHistory(@PageableDefault(size = 6) Pageable pageable,
                                                                            @RequestHeader(name = "userId") Long userId) {
        return ResponseEntity.ok(pointService.getMyPointTransaction(pageable, userId));
    }

    // p2p api [request only other microservices]

    @PutMapping
    public ResponseEntity<Void> updateMyPoint(@RequestBody PointUpdateRequest pointUpdateRequest,
                                              @RequestHeader(name = "userId") Long userId) {
        pointService.updatePoint(userId, pointUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createPointInfo(@RequestHeader(name = "userId") Long userId) {
        pointService.createPointInfo(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removePointInfo(@RequestHeader(name = "userId") Long userId) {
        pointService.removePointInfo(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/global/give_reward")
    public ResponseEntity<Void> giveReward(@RequestBody GiveRewardDto giveRewardDto) {
        pointService.giveReward(giveRewardDto);
        return ResponseEntity.ok().build();
    }
}
