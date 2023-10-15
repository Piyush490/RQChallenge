package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NoEmployeeFoundException extends RuntimeException {
    private Integer status;
    private String message;
}
