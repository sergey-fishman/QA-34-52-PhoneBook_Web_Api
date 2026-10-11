package dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageDto {
    private String timestamp;
    private Integer status;
    private String error;
    private Object message;
    private String path;
}
