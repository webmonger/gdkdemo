package com.gdkdemo.sensor.environmental.cp.core;


public final class IDPair
{
    private final Long id;
    private final String guid;

    public IDPair(Long id, String guid)
    {
        // TBD: Validation?
        this.id = id;
        this.guid = guid;
    }
    
    public Long id()
    {
        return id;
    }

    public String guid()
    {
        return guid;
    }
    
    public int hashCode()
    { 
        int hashId = id == null ? 0 : id.hashCode(); 
        int hashGuid = guid == null ? 0 : guid.hashCode(); 
        return (hashId + hashGuid) * hashGuid + hashId; 
    } 
 
    @Override
    public String toString() 
    {  
        return "(" + id + ", " + guid + ")";  
    }

}
