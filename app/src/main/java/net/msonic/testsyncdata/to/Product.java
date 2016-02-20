package net.msonic.testsyncdata.to;

/**
 * Created by manuelzegarra on 12/02/16.
 */
public class Product {


    public String id;
    public String code;
    public String name;
    public int counterFromServer; // counter_lastsync value of server counter when last sync from server was done ($this->syncFromServer)
    public int counterToServer; // servercounter_lastsync value of $this->counter when last sync to server was done ($this->syncToServer)
    public int deleted;
    public long timeStampUpdated;


}
