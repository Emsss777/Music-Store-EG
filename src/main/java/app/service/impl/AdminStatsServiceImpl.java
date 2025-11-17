package app.service.impl;

import app.mapper.TopSellingAlbumMapper;
import app.model.dto.AdminStatsDTO;
import app.model.dto.TopSellingAlbumDTO;
import app.repository.OrderItemRepo;
import app.repository.OrderRepo;
import app.repository.UserRepo;
import app.service.AdminStatsService;
import app.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminStatsServiceImpl implements AdminStatsService {

    private final AlbumService albumService;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final OrderItemRepo orderItemRepo;

    @Override
    @Cacheable("adminStats")
    public AdminStatsDTO getCurrentStats() {

        long totalAlbums = albumService.getTotalAlbumCount();
        BigDecimal totalRevenue = orderRepo.findTotalRevenue();
        long activeUsers = userRepo.countByIsActiveTrue();

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
        long ordersToday = orderRepo.countByCreatedOnBetween(startOfDay, endOfDay);
        long allOrders = orderRepo.count();

        List<TopSellingAlbumDTO> topSelling = orderItemRepo.findTopSellingAlbums()
                .stream()
                .map(TopSellingAlbumMapper::toDTO)
                .limit(3)
                .collect(Collectors.toList());

        return AdminStatsDTO.builder()
                .totalAlbums(totalAlbums)
                .totalRevenue(totalRevenue)
                .activeUsers(activeUsers)
                .ordersToday(ordersToday)
                .allOrders(allOrders)
                .topSellingAlbums(topSelling)
                .build();
    }
}
