package ir.bigz.springTest.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Generated
    private Long productId;

    @Column(unique = true)
    private String name;
    private Double price;

}
