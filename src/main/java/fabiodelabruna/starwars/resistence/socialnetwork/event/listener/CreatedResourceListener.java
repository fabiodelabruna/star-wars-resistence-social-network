package fabiodelabruna.starwars.resistence.socialnetwork.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import fabiodelabruna.starwars.resistence.socialnetwork.event.CreatedResourceEvent;

@Component
public class CreatedResourceListener implements ApplicationListener<CreatedResourceEvent> {

    @Override
    public void onApplicationEvent(final CreatedResourceEvent event) {
        final HttpServletResponse response = event.getResponse();
        final Long resourceId = event.getResourceId();
        setHeaderLocation(response, resourceId);
    }

    private void setHeaderLocation(final HttpServletResponse response, final Long resourceId) {
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(resourceId).toUri();
        response.setHeader("Location", uri.toASCIIString());
    }

}
