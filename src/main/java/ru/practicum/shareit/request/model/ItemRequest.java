package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_request")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_request_id")
    private long id;
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "requester_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;
    @Column(name = "created")
    private LocalDateTime created;

}
