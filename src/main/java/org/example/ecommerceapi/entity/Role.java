package org.example.ecommerceapi.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author $(bilal belhaj)
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
