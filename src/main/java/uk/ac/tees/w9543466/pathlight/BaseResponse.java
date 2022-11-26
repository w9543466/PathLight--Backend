package uk.ac.tees.w9543466.pathlight;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class BaseResponse<T> {

    private T data;
    private boolean success;
    private String message;
    private ErrorCode errorCode;
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

    public String getErrorCode() {
        return errorCode == null ? null : errorCode.getCode();
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseEntity<BaseResponse<T>> fail(String msg, HttpStatus status) {
        return fail(msg, null, status);
    }

    public static <T> ResponseEntity<BaseResponse<T>> fail(String msg, ErrorCode errorCode, HttpStatus status) {
        BaseResponse<T> response = new BaseResponse<>(false, msg);
        if (errorCode != null) {
            response.setErrorCode(errorCode);
        }
        return new ResponseEntity<>(response, status);
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
