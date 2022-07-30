package com.challengers.pointservice.point.service;

import com.challengers.pointservice.point.domain.Point;
import com.challengers.pointservice.point.domain.PointHistory;
import com.challengers.pointservice.point.domain.PointHistoryType;
import com.challengers.pointservice.point.dto.PointHistoryResponse;
import com.challengers.pointservice.point.dto.PointResponse;
import com.challengers.pointservice.point.dto.PointUpdateRequest;
import com.challengers.pointservice.point.repository.PointHistoryRepository;
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
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional(readOnly = true)
    public PointResponse getMyPoint(Long userId) {
        return new PointResponse(
                pointRepository.findByUserId(userId)
                        .orElseThrow(NoSuchElementException::new)
                        .getPoint()
        );
    }

    @Transactional(readOnly = true)
    public Page<PointHistoryResponse> getMyPointHistory(Pageable pageable, Long userId) {
        Point point = pointRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);

        return pointHistoryRepository.getPointHistory(pageable, point.getId());
    }

    @Transactional
    public void updatePoint(Long userId, PointUpdateRequest request) {
        Point point = pointRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        if (request.getPointHistory() < 0L && point.getPoint() < request.getPointHistory()*-1)
            throw new RuntimeException("포인트가 부족합니다.");
        pointHistoryRepository.save(
                new PointHistory(point,
                        request.getPointHistory(),
                        PointHistoryType.of(request.getPointHistoryType())
                ));
        point.updatePoint(request.getPointHistory());
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

        pointHistoryRepository.deleteByPointId(point.getId());
        pointRepository.delete(point);
    }
}
