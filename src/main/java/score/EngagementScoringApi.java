package score;

import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@Path("/api")
public class EngagementScoringApi {

  @GET
  @Path("template")
  public Uni<List<Template>> listTemplates() {
    return Template.<Template>listAll();
  }

  @POST
  @Path("template")
  @Consumes("application/json")
  @Produces("application/json")
  @ReactiveTransactional
  public Uni<Response> addTemplate(Template template) {
    return Panache.<Template>withTransaction(template::persist)
            .onItem().transform(inserted -> {
              return createdResponse("/template/%d", inserted.id);
        });
  }


  @POST
  @Path("/{tid}/section")
  @Consumes("application/json")
  @Produces("application/json")
  @ReactiveTransactional
  public Uni<Response> addSectionToTemplate(@PathParam("tid") Long tid, Section section) {

    return Template.<Template>findById(tid)
      .map(t -> {
        if (t != null) {
        section.setTemplate(t);
        section.persist();
        t.sections.add(section);
        return createdResponse("/template/%d/section/%d", tid, section.id);          
        } else {
            return notFoundResponse();
        }  

      });
    
  }
  
  private Response createdResponse(String s, Object... args) {
    return Response.created(URI.create(String.format(s, args))).build();
  }
  
  private Response notFoundResponse() {
    return Response.status(Status.NOT_FOUND).build();
  } 
    
  // private Uni<Response> createdResponseUni(String s, Object... args) {
  //   return Uni.createFrom().item(createdResponse(s, args));
  // }
  
  // private Uni<Response> notFoundResponseUni() {
  //   return Uni.createFrom().item(notFoundResponse());
  // } 
  
}
