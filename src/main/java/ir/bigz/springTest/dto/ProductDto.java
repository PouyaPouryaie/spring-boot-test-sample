package ir.bigz.springTest.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class ProductDto implements Serializable {

    private Long productId;
    private String name;
    private Double price;
}
