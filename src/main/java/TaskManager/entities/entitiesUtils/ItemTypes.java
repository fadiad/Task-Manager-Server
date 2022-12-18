package TaskManager.entities.entitiesUtils;

public enum ItemTypes {
    TASK(1),
    BUG(2),
    SUBTASK(3),
    TESTING(4);

    private final int taskNumber;

    private ItemTypes(int taskNumber){
        this.taskNumber=taskNumber;
    }
    public int getSize() {
        return taskNumber;
    }
}
