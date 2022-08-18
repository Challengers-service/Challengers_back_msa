package com.challengers.pointservice.point.service;

import com.challengers.pointservice.point.domain.Point;
import com.challengers.pointservice.point.domain.PointTransaction;
import com.challengers.pointservice.point.domain.PointTransactionType;
import com.challengers.pointservice.point.dto.PointTransactionResponse;
import com.challengers.pointservice.point.dto.PointResponse;
import com.challengers.pointservice.point.dto.PointUpdateRequest;
import com.challengers.pointservice.point.global.dto.GiveRewardDto;
import com.challengers.pointservice.point.repository.PointTransactionRepository;
import com.challengers.pointservice.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final PointTransactionRepository pointTransactionRepository;

    @Transactional(readOnly = true)
    public PointResponse getMyPoint(Long userId) {
        return new PointResponse(
                pointRepository.findByUserId(userId)
                        .orElseThrow(NoSuchElementException::new)
                        .getPoint()
        );
    }

    @Transactional(readOnly = true)
    public Page<PointTransactionResponse> getMyPointTransaction(Pageable pageable, Long userId) {
        Point point = pointRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);

        return pointTransactionRepository.getPointTransaction(pageable, point.getId());
    }

    @Transactional
    public void updatePoint(Long userId, PointUpdateRequest request) {
        Point point = pointRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        if (request.getPointHistory() < 0L && point.getPoint() < request.getPointHistory()*-1)
            throw new RuntimeException("포인트가 부족합니다.");
        pointTransactionRepository.save(
                new PointTransaction(point,
                        request.getPointHistory(),
                        PointTransactionType.of(request.getPointHistoryType())
                ));
        point.updatePoint(request.getPointHistory());
    }

    @Transactional
    public void giveReward(GiveRewardDto giveRewardDto) {
        Point point = pointRepository.findByUserId(giveRewardDto.getUserId()).orElseThrow(NoSuchElementException::new);

        pointTransactionRepository.save(
                new PointTransaction(point,
                        giveRewardDto.getReward(),
                        PointTransactionType.SUCCESS
                ));
        point.updatePoint(giveRewardDto.getReward());
    }

    @Transactional
    public void createPointInfo(Long userId) {
        if (pointRepository.findByUserId(userId).isPresent())
            throw new RuntimeException();

        pointRepository.save(Point.create(userId));
    }

    @Transactional
    public void removePointInfo(Long userId) {
        Point point = pointRepository.findByUserId(userId).orElseThrow();

        pointTransactionRepository.deleteByPointId(point.getId());
        pointRepository.delete(point);
    }
}
