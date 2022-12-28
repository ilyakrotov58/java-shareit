package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "items", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "item_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "availability")
    private Boolean available;

    @OneToMany
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private List<Comment> comments = new ArrayList<>();
}
