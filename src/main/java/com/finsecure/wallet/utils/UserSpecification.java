package com.finsecure.wallet.utils;

import java.lang.invoke.SerializedLambda;

import com.finsecure.wallet.model.User;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecification {
    public Specification<User> searchUser(String searchTerm) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            criteriaQuery.orderBy(new Order[]{criteriaBuilder.asc(root.get("userId"))});
            Predicate predicateForNone;
            if (searchTerm != null && !searchTerm.equals("")) {
                predicateForNone = criteriaBuilder.or(new Predicate[]{criteriaBuilder.like(criteriaBuilder.upper(root.get("firstName")), "%" + searchTerm.toUpperCase() + "%"), criteriaBuilder.like(criteriaBuilder.upper(root.get("lastName")), "%" + searchTerm.toUpperCase() + "%"), criteriaBuilder.like(criteriaBuilder.upper(root.get("userName")), "%" + searchTerm.toUpperCase() + "%"), criteriaBuilder.like(criteriaBuilder.upper(root.get("mobile")), "%" + searchTerm.toUpperCase() + "%"), criteriaBuilder.like(criteriaBuilder.upper(root.get("email")), "%" + searchTerm.toUpperCase() + "%"), criteriaBuilder.like(criteriaBuilder.upper(root.get("designation")), "%" + searchTerm.toUpperCase() + "%")});
                return criteriaBuilder.and(new Predicate[]{predicateForNone});
            } else {
                predicateForNone = criteriaBuilder.disjunction();
                return criteriaBuilder.or(new Predicate[]{predicateForNone});
            }
        };
    }
}