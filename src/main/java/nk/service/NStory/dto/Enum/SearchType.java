package nk.service.NStory.dto.Enum;

public enum SearchType {
    title(1),
    contents(2),
    author(3);

    private final int i;

    SearchType(int i) {
        this.i = i;
    }

    public int getType() {
        return i;
    }
}
