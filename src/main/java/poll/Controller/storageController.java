package poll.Controller;

import poll.View.moderatorView;
import poll.View.pollViewController;

import java.util.List;
import java.util.Set;

public interface storageController {
    public void addModerator(int id, moderatorView m);

    public moderatorView getModerator(int id);

    public void updateModerator(int id, moderatorView m);

    void addPoll(String id, pollInterfaceController p, int modId);

    public pollViewController getPoll(String id);

    public pollInterfaceController getPollFromMod(int modId, String pollId);

    public Set<String> getAllPollIDsFromMod(int modId);

    public void deletePoll(int modId, String pollId);

    public void vote(String pollId, int val);

    public List<pollInterfaceController> getExipredPolls(int modId);

    public List<moderatorView> getAllMods();

}
