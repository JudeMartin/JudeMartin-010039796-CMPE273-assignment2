package poll.Controller;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import poll.View.moderatorView;
import poll.View.mongoView;
import poll.View.pollViewController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class VoteController {
    private static AtomicInteger modIdGen = new AtomicInteger();
    private static AtomicInteger pollIdGen = new AtomicInteger();


    @Autowired
    storageController storageController = new mongoView();


    @RequestMapping(value = "/api/v1/moderators{id}", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public moderatorView addModerator(@RequestBody @Valid moderatorView input) {

        int id = getAllModeratorID(input);
        setAllModeratorID(input, id);
        return displayGeneratedID(input);
    }

    private moderatorView displayGeneratedID(moderatorView input) {
        return (input);
    }

    private void setAllModeratorID(moderatorView input, int id) {
        storageController.addModerator(id, input);
    }

    private int getAllModeratorID(moderatorView input) {
        int id = modIdGen.incrementAndGet();
        input.setId(id);
        return id;
    }


    @RequestMapping(value = "/api/v1/moderators/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public moderatorView getModerator(@PathVariable("id") int id) throws Exception {
        return storageController.getModerator(id);
    }


    @RequestMapping(value = "/api/v1/moderators/{id}", consumes = "application/json", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public moderatorView updateModerator(@PathVariable("id") int id, @RequestBody @Valid moderatorView input) throws Exception {
        moderatorView m = storageController.getModerator(id);
        getModeratorDetails(id, input, m);
        return m;
    }

    private void getModeratorDetails(int id, moderatorView input, moderatorView m) {
        if (m != null) {
            setModeratorDetails(input, m);
        }
        storageController.updateModerator(id, m);
    }

    private void setModeratorDetails(moderatorView input, moderatorView m) {
        setModeratorName(input, m);
        setModeratorEmail(input, m);
        if (input.getPassword() != null) {
            m.setPassword(input.getPassword());
        }
    }

    private void setModeratorEmail(moderatorView input, moderatorView m) {
        if (input.getEmail() != null) {
            m.setEmail(input.getEmail());
        }
    }

    private void setModeratorName(moderatorView input, moderatorView m) {
        if (input.getName() != null) {
            m.setName(input.getName());
        }
    }


    @RequestMapping(value = "/api/v1/moderators/{modId}/polls", consumes = "application/json", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public pollInterfaceController addPoll(@RequestBody @Valid pollViewController input, @PathVariable("modId") int modId) throws Exception {
        String id36 = getGeneratedPollID(input);
        addGeneratedPoll(input, modId, id36);
        return returnGeneratePollID(input);
    }

    private pollInterfaceController returnGeneratePollID(pollViewController input) {
        return input;
    }

    private void addGeneratedPoll(pollViewController input, int modId, String id36) {
        storageController.addPoll(id36, input, modId);
    }

    private String getGeneratedPollID(pollViewController input) {
        int id = pollIdGen.incrementAndGet();
        String id36 = Integer.toString(id, 36);
        input.setId(id36);
        return id36;
    }


    @RequestMapping(value = "/api/v1/polls/{id}", produces = "application/json", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getPoll(@PathVariable("id") String id) throws JsonProcessingException {
        pollInterfaceController poll = storageController.getPoll(id);
        Set<String> allowedFields = new HashSet<String>();
        addFields(allowedFields);

        @JsonFilter("myFilter")
        class ResultsFilterMixin {
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixInAnnotations(pollViewController.class, ResultsFilterMixin.class);
        FilterProvider filters = getFilterProvider(allowedFields);
        setFilterProvider(mapper, filters);
        return mapper.writeValueAsString(poll);
    }

    private void setFilterProvider(ObjectMapper mapper, FilterProvider filters) {
        mapper.setFilters(filters);
    }

    private FilterProvider getFilterProvider(Set<String> allowedFields) {
        return new SimpleFilterProvider().addFilter("myFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept(allowedFields));
    }


    private void addFields(Set<String> allowedFields) {
        allowedFields.add("id");
        allowedFields.add("question");
        allowedFields.add("started_at");
        allowedFields.add("expired_at");
        allowedFields.add("choice");
    }


    @RequestMapping(value = "/api/v1/moderators/{modId}/polls/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public pollInterfaceController getPollWithResults(@PathVariable("id") String id, @PathVariable("modId") int modId) throws Exception {
        return storageController.getPollFromMod(modId, id);
    }


    @RequestMapping(value = "/api/v1/moderators/{modId}/polls", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public pollInterfaceController[] getAllPolls(@PathVariable("modId") int modId) throws Exception {
        Set<String> mp = storageController.getAllPollIDsFromMod(modId);
        if (pollRetriever(mp)) return getRequiredPolls(mp);
        return null;
    }

    private boolean pollRetriever(Set<String> mp) {
        if (mp != null) {
            return true;
        }
        return false;
    }

    private pollInterfaceController[] getRequiredPolls(Set<String> mp) {
        pollInterfaceController[] res = new pollInterfaceController[mp.size()];
        int i = 0;
        updateIndividualPolls(mp, res, i);
        return res;
    }

    private void updateIndividualPolls(Set<String> mp, pollInterfaceController[] res, int i) {
        for (String pid : mp) {
            res[i] = storageController.getPoll(pid);
            i++;
        }
    }

    // Delete a Poll
    @RequestMapping(value = "/api/v1/moderators/{modId}/polls/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public pollInterfaceController deletePoll(@PathVariable("id") String id, @PathVariable("modId") int modId) throws Exception {
        return delPoll(id, modId);
    }

    private pollInterfaceController delPoll(String id, int modId) {
        storageController.deletePoll(modId, id);
        return null;
    }

    // Vote a poll
    @RequestMapping(value = "/api/v1/polls/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void votePoll(@PathVariable("id") String id, @RequestParam(value = "choice") int val) {
        storageController.vote(id, val);
    }

}