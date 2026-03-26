package com.finsecure.wallet.common;

import java.io.Serializable;

public class ServiceOutcome<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T data;
    private boolean outcome;
    private String message;

    // Default constructor (success = true)
    public ServiceOutcome() {
        this.outcome = true;
    }

    // All-args constructor
    public ServiceOutcome(T data, boolean outcome, String message) {
        this.data = data;
        this.outcome = outcome;
        this.message = message;
    }

    // Static factory methods (BEST PRACTICE)
    public static <T> ServiceOutcome<T> success(T data) {
        return new ServiceOutcome<>(data, true, "Success");
    }

    public static <T> ServiceOutcome<T> success(T data, String message) {
        return new ServiceOutcome<>(data, true, message);
    }

    public static <T> ServiceOutcome<T> failure(String message) {
        return new ServiceOutcome<>(null, false, message);
    }

    // Getters & Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOutcome() {
        return outcome;
    }

    public void setOutcome(boolean outcome) {
        this.outcome = outcome;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
