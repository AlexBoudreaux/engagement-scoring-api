package score;

import java.net.URI;
import java.util.List;


import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

@Path("/api")
public class EngagementScoringApi {

  @GET
  @Path("template")
  @Blocking
  public Uni<List<Template>> listTemplates() {
  
    return Uni.createFrom().item(Template.<Template>listAll()).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());

  }

  @Transactional
  @POST
  @Path("template")
  @Consumes("application/json")
  @Produces("application/json")
  public Uni<Response> addTemplate(Template template) {

    template.persist();
        
    return createdResponseUni("/template/%d", template.id);

  }


  @Transactional
  @POST
  @Path("/{tid}/section")
  @Consumes("application/json")
  @Produces("application/json")
  public Uni<Response> addSectionToTemplate(@PathParam("tid") Long tid, Section section) {

      Template t = Template.<Template>findById(tid);

      if (t != null) {
        section.setTemplate(t);
          section.persist();
      
          t.sections.add(section);
      
          return createdResponseUni("/template/%d/section/%d", tid, section.id);
      } else {
        return notFoundResponseUni();
      }
  }
  
  private Response createdResponse(String s, Object... args) {
    return Response.created(URI.create(String.format(s, args))).build();
  }
  
  private Response notFoundResponse() {
    return Response.status(Status.NOT_FOUND).build();
  } 
    
  private Uni<Response> createdResponseUni(String s, Object... args) {
    return Uni.createFrom().item(createdResponse(s, args));
  }
  
  private Uni<Response> notFoundResponseUni() {
    return Uni.createFrom().item(notFoundResponse());
  } 
  
}
