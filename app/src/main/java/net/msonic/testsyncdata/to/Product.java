package net.msonic.testsyncdata.to;

/**
 * Created by manuelzegarra on 12/02/16.
 */
public class Product {


    public String id;
    public String code;
    public String name;
    //public int counterFromServer; // counter_lastsync value of server counter when last sync from server was done ($this->syncFromServer)
    //public int counterToServer; // servercounter_lastsync value of $this->counter when last sync to server was done ($this->syncToServer)
    public int counterUpdate;
    public int deleted;
    public long timeStampUpdated;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("id:%s ",id));
        sb.append(String.format("code:%s ",code));
        sb.append(String.format("name:%s ",name));
        sb.append(String.format("counterFromServer:%s ",counterUpdate));
        //sb.append(String.format("counterToServer:%s ",counterToServer));
        sb.append(String.format("deleted:%s ",deleted));
        sb.append(String.format("timeStampUpdated:%s ",timeStampUpdated));
        sb.append("\n");
        return sb.toString();
    }
}
