package ir.bigz.springTest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ProductDto {

    private final Long productId;
    private final String name;
    private final Double price;
}
