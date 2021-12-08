package fabiodelabruna.starwars.resistence.socialnetwork.event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

import lombok.Getter;

@Getter
public class CreatedResourceEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final HttpServletResponse response;
    private final Long resourceId;

    public CreatedResourceEvent(final Object source, final HttpServletResponse response, final Long resourceId) {
        super(source);
        this.response = response;
        this.resourceId = resourceId;
    }

}
