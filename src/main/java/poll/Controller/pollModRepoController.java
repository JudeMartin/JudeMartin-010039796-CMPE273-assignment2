package poll.Controller;

import org.springframework.data.mongodb.repository.MongoRepository;
import poll.View.moderatorPollView;

import java.util.List;

public interface pollModRepoController extends MongoRepository<moderatorPollView, String> {
    public moderatorPollView findByModIdAndPollId(int modId, String pollId);

    public List<moderatorPollView> findAllByModId(int modId);
}
