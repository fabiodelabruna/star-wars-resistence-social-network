package fabiodelabruna.starwars.resistence.socialnetwork.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "rebel")
public class Rebel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer age;

    @JsonIgnore
    @Column(name = "traitor_reports")
    private Integer traitorReports;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Valid
    @NotNull
    @Embedded
    private Localization localization;

    @Valid
    @NotNull
    @OneToMany(mappedBy = "rebel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<InventoryItem> inventory;

    @Transient
    @Getter(AccessLevel.NONE)
    private boolean traitor;

    public void reportTraitor() {
        if (traitorReports == null) {
            traitorReports = 0;
        }
        traitorReports++;
    }

    public boolean isTraitor() {
        return traitorReports != null && traitorReports >= 3;
    }

}
