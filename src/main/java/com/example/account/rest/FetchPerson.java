package com.example.account.rest;

import com.common.Person;
import org.platformlambda.core.exception.AppException;
import org.platformlambda.core.models.EventEnvelope;
import org.platformlambda.core.system.EventEmitter;
import org.platformlambda.core.system.PostOffice;
import org.platformlambda.core.util.AppConfigReader;
import org.platformlambda.core.util.Utility;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

//@PreLoad(route = "v1.fetch.person", instances = 100)
@RestController
public class FetchPerson {


//    @Override
//    public Object handleEvent(Map<String, String> headers, Object input, int instance) throws Exception {
//        AppConfigReader config = AppConfigReader.getInstance();
//       //Person person = new Person();
////        person.setId(123);
////        person.setName("user");
////        person.setAddress("user address");
//
//        String remotePort = config.getProperty("lambda.example.port", "8100");
//        String remoteEndpoint = "http://127.0.0.1:"+remotePort+"/api/profile";
//        String traceId = Utility.getInstance().getUuid();
//        PostOffice po = new PostOffice("com.example.models", traceId, "GET /api/profile");
//
//
//        //EventEmitter poe = EventEmitter.getInstance();
//        EventEnvelope req = new EventEnvelope().setTo("v1.get.profile");
//
//
//        EventEnvelope  response = po.request(req, 3000,false).get();
//        // poe.send(event);
//            try {
//
//                if (response.getBody() instanceof Person result) {
//                    return result;
//                }
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            return  response.getBody();
//}
@GetMapping("/api/fetchprofile")
    public Flux<List<Person>> getPoJo() throws IOException, ExecutionException, InterruptedException {
    AppConfigReader config = AppConfigReader.getInstance();

    String traceId = Utility.getInstance().getUuid();
    PostOffice po = new PostOffice("person.test.endpoint", traceId, "GET /api/profile");
    EventEmitter eventemitter = EventEmitter.getInstance();
    EventEnvelope req1= new EventEnvelope().setTo("person.pojo");
    String remoteEndpoint = "http://127.0.0.1:8100/api/eventkk";
//    eventemitter.send(req1);

        return Flux.create(callback -> {
            try {
                EventEnvelope response = po.request(req1, 3000, Collections.emptyMap(), remoteEndpoint, true).get();
//                if (response.getBody() instanceof Person result) {
            //    if (Person.class.getName().equals(response.getType())) {
                    List<Person> per = response.getBody(List.class);

                    EventEnvelope req2 = new EventEnvelope().setTo("person.identify").setBody(per);
                    eventemitter.send(req2);
             //   callback.complete();
         //       }
            // else {
                    callback.error(new AppException(response.getStatus(), response.getError()));
            //    }
            } catch (Exception  e) {
                callback.error(e);
            }
        });

    }
}
