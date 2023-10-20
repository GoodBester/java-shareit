package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;


/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long id;
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    @Column(name = "available")
    private boolean available;
    @JoinColumn(name = "owner")
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;
    @Column(name = "count_rented")
    private int countRented;


}
