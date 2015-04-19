package poll.Controller;

import org.springframework.data.mongodb.repository.MongoRepository;
import poll.View.pollViewController;

public interface pollRepoController extends MongoRepository<pollViewController, String> {

}
