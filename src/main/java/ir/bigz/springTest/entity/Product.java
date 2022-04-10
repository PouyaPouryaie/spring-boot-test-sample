package ir.bigz.springTest.entity;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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
