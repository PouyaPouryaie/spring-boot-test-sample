package ir.bigz.springTest.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ProductDto implements Serializable {

    private Long productId;
    private String name;
    private Double price;
}
