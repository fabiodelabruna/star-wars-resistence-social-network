package fabiodelabruna.starwars.resistence.socialnetwork.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class Localization {

    @NotNull
    @Column(name = "localization")
    private String name;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

}
