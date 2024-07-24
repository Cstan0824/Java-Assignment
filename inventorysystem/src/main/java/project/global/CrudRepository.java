package project.global;

public interface CrudRepository<ID> {
//Create
    public boolean Create();

    //Read
    public void Read(ID _Id);

    //Update
    public boolean Update(ID _Id);

    //Delete 
    public boolean Remove(ID _Id);
}
