package az.digirella.assignment.currency.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ulphat
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectResultDto {
    private String code;
    private String message;
    public boolean isSuccess() {
        return "0000".equals(code);
    }

    public static CollectResultDto success() {
        return new CollectResultDto("0000", "Məlumatlar daxil edildi");
    }

    public static CollectResultDto exists() {
        return new CollectResultDto("0001", "Bu tarixə aid məlumatlar bazada mövcuddur");
    }

    public static CollectResultDto notFound() {
        return new CollectResultDto("0002", "Məlumat tapılmadı");
    }

    public static CollectResultDto externalException(String message) {
        return new CollectResultDto("0003", "message");
    }
}
