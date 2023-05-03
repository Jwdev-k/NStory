package nk.service.NStory.dto.record;

import lombok.Getter;

@Getter
public class RecordLogList extends RecordLogDTO{
    private final int aid;

    public RecordLogList(int id, String contents, String email, String name, String time, int aid) {
        super(id, contents, email, name, time);
        this.aid = aid;
    }
}
