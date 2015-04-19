package poll.View;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import poll.Controller.pollInterfaceController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

public class pollViewController implements pollInterfaceController {
    public static final SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ'Z'");
    @Id
    String id;
    @NotNull
    @Size(min = 1)
    String question;
    @NotNull
    Date started_at;
    @NotNull
    Date expired_at;
    @NotNull
    @NotEmpty
    String[] choice;
    int[] results;


    public pollViewController() {
        super();
        setTime();
    }

    private void setTime() {
        this.started_at = new Date(System.currentTimeMillis());
        this.expired_at = new Date(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStarted_at() {

        return df1.format(started_at);
    }

    public void setStarted_at(Date started_at) {
        this.started_at = started_at;
    }

    public String getExpired_at() {
        return df1.format(expired_at);
    }

    public void setExpired_at(Date expired_at) {
        this.expired_at = expired_at;
    }

    public String[] getChoice() {
        return choice;
    }

    public void setChoice(String[] choice) {
        choiceCreater(choice);
        resultsCreater(choice);
    }

    private void choiceCreater(String[] choice) {
        this.choice = choice;
    }

    private void resultsCreater(String[] choice) {
        results = new int[choice.length];
    }

    public int[] getResults() {
        return results;
    }

    public void setResults(int[] results) {
        this.results = results;
    }

    @Override
    public void vote(int val) {
        if (val < results.length) {
            incrementResults(val);
        }
    }

    private void incrementResults(int val) {
        results[val]++;
    }
}