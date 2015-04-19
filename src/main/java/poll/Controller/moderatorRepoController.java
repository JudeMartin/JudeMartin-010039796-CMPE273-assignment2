package poll.Controller;

import org.springframework.data.mongodb.repository.MongoRepository;
import poll.View.moderatorView;

public interface moderatorRepoController extends MongoRepository<moderatorView, Integer> {

}
