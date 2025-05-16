package com.example.headachediary.presentation.viewmodel;


public class StateBase<T> {

    public enum Status { LOADING, SUCCESS, ERROR }

    private final Status status;
    private final T data;
    private final String error;

    // Конструкторы
    public StateBase(Status status, T data, String error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static StateBase loading() {
        return new StateBase(Status.LOADING, null, null);
    }

    public static <T>StateBase success(T data) {
        return new StateBase(Status.SUCCESS, data, null);
    }

    public static StateBase error(String error) {
        return new StateBase(Status.ERROR, null, error);
    }

    // Геттеры
    public StateBase.Status getStatus() { return status; }
    public T getData() { return data; }
    public String getError() { return error; }

}
