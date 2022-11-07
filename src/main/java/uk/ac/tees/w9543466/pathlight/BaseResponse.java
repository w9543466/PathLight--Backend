package uk.ac.tees.w9543466.pathlight;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class BaseResponse<T> {

    private T data;
    private boolean success;
    private String message;
    private List<String> errors;

    public BaseResponse(T data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public BaseResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addToErrors(String error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResponseEntity<BaseResponse<Void>> fail(String msg, HttpStatus status) {
        return new ResponseEntity<>(new BaseResponse<>(false, msg), status);
    }

    public static ResponseEntity<BaseResponse<Void>> success(String msg, HttpStatus status) {
        return new ResponseEntity<>(new BaseResponse<>(true, msg), status);
    }

    public static ResponseEntity<BaseResponse<Void>> ok(String msg) {
        return success(msg, HttpStatus.OK);
    }

    public static <T> ResponseEntity<BaseResponse<T>> ok(T data) {
        return success(data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<BaseResponse<T>> success(T data, HttpStatus status) {
        return new ResponseEntity<>(new BaseResponse<>(data, true), status);
    }
}
