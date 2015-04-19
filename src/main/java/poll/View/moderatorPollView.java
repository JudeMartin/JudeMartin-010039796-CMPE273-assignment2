package poll.View;

import org.springframework.data.annotation.Id;

public class moderatorPollView {
    @Id
    String pollId;
    int modId;

    public moderatorPollView() {
        super();
    }

    public moderatorPollView(String id, int modId) {
        this.pollId = id;
        this.modId = modId;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

}
