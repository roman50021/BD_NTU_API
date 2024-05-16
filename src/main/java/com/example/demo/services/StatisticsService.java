package com.example.demo.services;

import com.example.demo.models.Dish;
import com.example.demo.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final CourierRepository courierRepository;
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final RestaurantAddressRepository restaurantAddressRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    @Cacheable("statistics")
    public Map<String, Long> generateStatistics() {
        Map<String, Long> statistics = new HashMap<>();


        long totalCourier = courierRepository.count();
        long totalDish = dishRepository.count();
        long totalMenu = menuRepository.count();
        long totalOrder = orderRepository.count();
        long totalRestaurantAddress = restaurantAddressRepository.count();
        long totalRestaurant = restaurantRepository.count();
        long totalUserAddress = userAddressRepository.count();
        long totalUser = userRepository.count();

        statistics.put("totalCourier", totalCourier);
        statistics.put("totalDish", totalDish);
        statistics.put("totalMenu", totalMenu);
        statistics.put("totalOrder", totalOrder);
        statistics.put("totalRestaurantAddress", totalRestaurantAddress);
        statistics.put("totalRestaurant", totalRestaurant);
        statistics.put("totalUserAddress", totalUserAddress);
        statistics.put("totalUser", totalUser);

        return statistics;
    }

    public Map<String, Long> generateFilteredStatistics(Double minPrice, Double maxPrice, String searchTerm) {
        Map<String, Long> statistics = new HashMap<>();

        Specification<Dish> specification = Specification.where(null);
        if (minPrice != null) {
            specification = specification.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            specification = specification.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        if (searchTerm != null && !searchTerm.isEmpty()) {
            String likeTerm = "%" + searchTerm + "%";
            specification = specification.and((root, query, builder) -> builder.or(
                    builder.like(root.get("name"), likeTerm),
                    builder.like(root.get("description"), likeTerm)
            ));
        }

        List<Dish> filteredDishes = dishRepository.findAll(specification);

        long totalDishes = filteredDishes.size();

        statistics.put("totalDishes", totalDishes);

        return statistics;
    }
}
