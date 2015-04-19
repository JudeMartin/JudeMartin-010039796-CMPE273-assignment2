package poll.View;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poll.Controller.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class mongoView implements storageController {
    @Autowired
    private moderatorRepoController mRep;
    @Autowired
    private pollRepoController pRep;
    @Autowired
    private pollModRepoController pmRep;

    @Override
    public void addModerator(int id, moderatorView m) {
        mRep.save(m);
    }

    @Override
    public moderatorView getModerator(int id) {
        return mRep.findOne(id);
    }

    @Override
    public void updateModerator(int id, moderatorView m) {
        mRep.save(m);
    }

    @Override
    public void addPoll(String id, pollInterfaceController p, int modId) {
        pRep.save(p);
        moderatorPollView pm = new moderatorPollView(id, modId);
        pmRep.save(pm);
    }

    @Override
    public pollViewController getPoll(String id) {
        return pRep.findOne(id);
    }

    @Override
    public pollInterfaceController getPollFromMod(int modId, String pollId) {
        moderatorPollView pm = pmRep.findByModIdAndPollId(modId, pollId);
        if (pm != null) {
            return pRep.findOne(pollId);
        }
        return null;
    }

    @Override
    public Set<String> getAllPollIDsFromMod(int modId) {
        List<moderatorPollView> lst = pmRep.findAllByModId(modId);
        if (lst != null) {
            return addToLIst(lst);
        }
        return null;
    }

    private Set<String> addToLIst(List<moderatorPollView> lst) {
        Set<String> pollIds = new HashSet<String>();
        for (moderatorPollView pm : lst) {
            pollIds.add(pm.getPollId());
        }
        return pollIds;
    }

    @Override
    public void deletePoll(int modId, String pollId) {
        moderatorPollView pm = pmRep.findByModIdAndPollId(modId, pollId);
        if (pm != null) {
            pRep.delete(pollId);
            pmRep.delete(pollId);
        }
    }

    @Override
    public void vote(String pollId, int val) {
        pollInterfaceController p = pRep.findOne(pollId);
        p.vote(val);
        pRep.save(p);
    }

    @Override
    public List<pollInterfaceController> getExipredPolls(int modId) {
        Set<String> pollIDs = getAllPollIDsFromMod(modId);
        long currTs = System.currentTimeMillis();
        List<pollInterfaceController> expPolls = new ArrayList<pollInterfaceController>();
        expirationAddPolls(pollIDs, currTs, expPolls);
        return expPolls;
    }

    private void expirationAddPolls(Set<String> pollIDs, long currTs, List<pollInterfaceController> expPolls) {
        for (String id : pollIDs) {
            pollViewController p = getPoll(id);
            System.out.println(currTs + " - " + p.expired_at.getTime());
            if (p.expired_at.getTime() < currTs) {
                expPolls.add(p);
            }
        }
    }

    @Override
    public List<moderatorView> getAllMods() {
        return mRep.findAll();
    }
}
