package poll.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import poll.Controller.pollInterfaceController;
import poll.Controller.storageController;
import poll.View.moderatorView;
import poll.View.mongoView;
import poll.View.pollViewController;

import java.util.List;

@Component
public class taskModel {

    ProducerModel prod = new ProducerModel();

    @Autowired
    storageController storageController = new mongoView();

    @Scheduled(fixedRate = 300000)
    public void getExpiredPolls() {
        List<moderatorView> mods = storageController.getAllMods();
        for (moderatorView m : mods) {
            getPollsToAppend(m);
        }
    }

    private void getPollsToAppend(moderatorView m) {
        List<pollInterfaceController> expPolls = storageController.getExipredPolls(m.id);
        if (!expPolls.isEmpty()) {
            for (pollViewController p : expPolls) {
                append(m, p);

            }

        }
    }

    private void append(moderatorView m, pollViewController p) {
        StringBuilder sb = new StringBuilder();
        appendEmail(m, sb);
        for (int i = 0; i < p.choice.length; i++) {
            appendDetails(p, sb, i);
        }
        appendMessage(sb);
    }

    private void appendMessage(StringBuilder sb) {
        sb.append("]");
        prod.sendMessage(sb.toString());
        System.out.println(sb.toString());
    }

    private void appendEmail(moderatorView m, StringBuilder sb) {
        sb.append(m.email);
        sb.append(":010039796:Poll Result [");
    }

    private void appendDetails(pollViewController p, StringBuilder sb, int i) {
        sb.append(p.choice[i]);
        sb.append("=");
        sb.append(p.results[i]);
        if (i < p.choice.length - 1) {
            sb.append(",");
        }
    }
}
